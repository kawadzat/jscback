package io.getarrays.securecapita.laptoplist;

import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.products.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LaptopListService {
    private final LaptopListRepository laptopListRepository;

    public LaptopListDto createLaptopList(LaptopListDto laptopListDto) {
        LaptopList laptopList = LaptopList.builder()
                .manufacturer(laptopListDto.getManufacturer())
                .model(laptopListDto.getModel())
                .build();
        LaptopList savedLaptopList = laptopListRepository.save(laptopList);
        return mapToDto(savedLaptopList);
    }

    public List<LaptopListDto> getAllLaptopLists() {
        return laptopListRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
//
//    public LaptopListDto getLaptopListById(Long id) {
//        LaptopList laptopList = laptopListRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Laptop list not found with id: " + id));
//        return mapToDto(laptopList);
//    }
//
//    public LaptopListDto updateLaptopList(Long id, LaptopListDto laptopListDto) {
//        LaptopList existingLaptopList = laptopListRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Laptop list not found with id: " + id));
//
//        existingLaptopList.setName(laptopListDto.getName());
//        LaptopList updatedLaptopList = laptopListRepository.save(existingLaptopList);
//        return mapToDto(updatedLaptopList);
//    }
//
//    public void deleteLaptopList(Long id) {
//        if (!laptopListRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Laptop list not found with id: " + id);
//        }
//        laptopListRepository.deleteById(id);
//    }
//
    private LaptopListDto mapToDto(LaptopList laptopList) {
        return LaptopListDto.builder()
                .id(laptopList.getId())
                .manufacturer(laptopList.getManufacturer())
                .model(laptopList.getModel())
                .build();
    }

    public boolean isDuplicate(LaptopListDto dto) {
        return laptopListRepository
                .findByManufacturerAndModel(dto.getManufacturer(), dto.getModel())
                .isPresent();
    }
}
