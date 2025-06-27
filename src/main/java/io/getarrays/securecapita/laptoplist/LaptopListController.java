package io.getarrays.securecapita.laptoplist;

import io.getarrays.securecapita.itinventory.LaptopService;
import io.getarrays.securecapita.products.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/laptoplist")
@RequiredArgsConstructor
public class LaptopListController {
private  final LaptopListService laptopListService;

    @PostMapping("/create")
    public ResponseEntity<?> createLaptopList(@RequestBody LaptopListDto laptopListDto) {
        if (laptopListService.isDuplicate(laptopListDto)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("A laptop with the same manufacturer and model already exists.");
        }

        LaptopListDto created = laptopListService.createLaptopList(laptopListDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LaptopListDto>> getAllLaptopLists() {
        return ResponseEntity.ok(laptopListService.getAllLaptopLists());
    }






}
