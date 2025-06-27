package io.getarrays.securecapita.department.service;

import io.getarrays.securecapita.asserts.service.StationService;
import io.getarrays.securecapita.codegenerator.CodeGeneratorService;
import io.getarrays.securecapita.department.dto.DepartmentDto;
import io.getarrays.securecapita.department.model.DepartmentEntity;
import io.getarrays.securecapita.department.repository.DepartmentRepository;
import io.getarrays.securecapita.domain.PageResponseDto;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StationService stationService;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Transactional
    public DepartmentDto createDepartment(DepartmentDto departmentDTO) {
        DepartmentEntity departmentEntity = createOrUpdateRequest(null, departmentDTO);
        return entityToDto(departmentRepository.save(departmentEntity));
    }

    public PageResponseDto<DepartmentDto> getAllDepartments(int page, int size) {
        Page<DepartmentEntity> departmentEntityPage = departmentRepository.findAll(PageRequest.of(page, size));
        return new PageResponseDto<DepartmentDto>(departmentEntityPage.getContent().stream().map(this::entityToDto).toList(), departmentEntityPage);
    }

    public DepartmentDto getDepartmentById(Long id) {
        return entityToDto(findDepartmentByIdOrThrow(id));
    }

    @Transactional
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDTO) {
        DepartmentEntity departmentEntity = createOrUpdateRequest(id, departmentDTO);
        return entityToDto(departmentRepository.save(departmentEntity));
    }

    @Transactional
    public boolean deleteDepartment(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }
        throw new ResourceNotFoundException("Department not found with id " + id);
    }

    private String generateDepartmentCode() {
        String prefix = "DEP-";
        int code = codeGeneratorService.generateCode("Department");

        return prefix + code;
    }

    private DepartmentEntity createOrUpdateRequest(Long id, DepartmentDto dto) {
        DepartmentEntity departmentEntity = null;
        if (id != null) {
            departmentEntity = findDepartmentByIdOrThrow(id);
        }
        if (dto != null) {
            if (departmentEntity == null) {
                departmentEntity = new DepartmentEntity();
                departmentEntity.setCode(generateDepartmentCode());
            }
            departmentEntity.setName(dto.getName());
        }
        return departmentEntity;
    }

    public DepartmentDto entityToDto(DepartmentEntity entity) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        return dto;
    }

    public DepartmentEntity findDepartmentByIdOrThrow(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not " +
                "found with id " + id));
    }

    public List<DepartmentEntity> findDepartmentsByIdsOrThrow(List<Long> ids) {
        List<DepartmentEntity> departments = departmentRepository.findAllById(ids);

        // Check if all requested IDs are found
        if (departments.size() != ids.size()) {
            List<Long> foundIds = departments.stream().map(DepartmentEntity::getId).toList();
            List<Long> missingIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();

            throw new ResourceNotFoundException("Departments not found with ids: " + missingIds);
        }

        return departments;
    }

}