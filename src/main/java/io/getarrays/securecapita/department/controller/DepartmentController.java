package io.getarrays.securecapita.department.controller;

import io.getarrays.securecapita.department.dto.DepartmentDto;
import io.getarrays.securecapita.department.service.DepartmentService;
import io.getarrays.securecapita.domain.PageResponseDto;
import io.getarrays.securecapita.exception.CustomMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // Create a new department
    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        DepartmentDto createdDepartment = departmentService.createDepartment(departmentDto);
        return new ResponseEntity<>(createdDepartment, HttpStatus.OK);
    }

    // Get all departments with pagination
    @GetMapping
    public ResponseEntity<PageResponseDto<DepartmentDto>> getAllDepartments(@RequestParam(value = "page",
            defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        PageResponseDto<DepartmentDto> departments = departmentService.getAllDepartments(page, size);
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    // Get department by id
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Long id) {
        return new ResponseEntity<>(departmentService.getDepartmentById(id), HttpStatus.OK);
    }

    // Update department by id
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id,
                                                          @Valid @RequestBody DepartmentDto departmentDto) {
        DepartmentDto updatedDepartment = departmentService.updateDepartment(id, departmentDto);
        return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }

    // Delete department by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>(new CustomMessage("Department deleted successfully"), HttpStatus.OK);
    }
}