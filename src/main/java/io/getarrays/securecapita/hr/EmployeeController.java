package io.getarrays.securecapita.hr;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("hr/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // Basic CRUD operations
    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.createWithDuplicateCheck(employee), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.updateWithDuplicateCheck(id, employee));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Search and filter endpoints
    @GetMapping("/employee-id/{employeeId}")
    public ResponseEntity<Employee> getByEmployeeId(@PathVariable String employeeId) {
        return ResponseEntity.ok(employeeService.getByEmployeeId(employeeId));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Employee> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(employeeService.getByEmail(email));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Employee>> getByStatus(@PathVariable EmployeeStatus status) {
        return ResponseEntity.ok(employeeService.getByStatus(status));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<Employee>> getByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(employeeService.getByDepartment(department));
    }

    @GetMapping("/job-title/{jobTitle}")
    public ResponseEntity<List<Employee>> getByJobTitle(@PathVariable String jobTitle) {
        return ResponseEntity.ok(employeeService.getByJobTitle(jobTitle));
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<Employee>> getByManager(@PathVariable Long managerId) {
        return ResponseEntity.ok(employeeService.getByManager(managerId));
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Employee>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.searchByName(name));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Employee>> getActiveEmployees() {
        return ResponseEntity.ok(employeeService.getActiveEmployees());
    }

    @GetMapping("/hired-between")
    public ResponseEntity<List<Employee>> getEmployeesHiredBetween(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(employeeService.getEmployeesHiredBetween(startDate, endDate));
    }

    @GetMapping("/salary-range")
    public ResponseEntity<List<Employee>> getEmployeesBySalaryRange(
            @RequestParam Double minSalary,
            @RequestParam Double maxSalary) {
        return ResponseEntity.ok(employeeService.getEmployeesBySalaryRange(minSalary, maxSalary));
    }

    @GetMapping("/department-status")
    public ResponseEntity<List<Employee>> getEmployeesByDepartmentAndStatus(
            @RequestParam String department,
            @RequestParam EmployeeStatus status) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartmentAndStatus(department, status));
    }

    // Business logic endpoints
    @PutMapping("/{id}/terminate")
    public ResponseEntity<Employee> terminateEmployee(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(employeeService.terminateEmployee(id, reason));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Employee> activateEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.activateEmployee(id));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Employee> deactivateEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.deactivateEmployee(id));
    }

    @PutMapping("/{id}/change-department")
    public ResponseEntity<Employee> changeDepartment(
            @PathVariable Long id,
            @RequestParam String newDepartment) {
        return ResponseEntity.ok(employeeService.changeDepartment(id, newDepartment));
    }

    @PutMapping("/{id}/change-job-title")
    public ResponseEntity<Employee> changeJobTitle(
            @PathVariable Long id,
            @RequestParam String newJobTitle) {
        return ResponseEntity.ok(employeeService.changeJobTitle(id, newJobTitle));
    }

    @PutMapping("/{id}/update-salary")
    public ResponseEntity<Employee> updateSalary(
            @PathVariable Long id,
            @RequestParam Double newSalary) {
        return ResponseEntity.ok(employeeService.updateSalary(id, newSalary));
    }

    // Reporting endpoints
    @GetMapping("/reports/department-counts")
    public ResponseEntity<Map<String, Long>> getEmployeeCountByDepartment() {
        return ResponseEntity.ok(employeeService.getEmployeeCountByDepartment());
    }

    @GetMapping("/reports/total-count")
    public ResponseEntity<Long> getTotalEmployeeCount() {
        return ResponseEntity.ok(employeeService.getTotalEmployeeCount());
    }

    @GetMapping("/reports/active-count")
    public ResponseEntity<Long> getActiveEmployeeCount() {
        return ResponseEntity.ok(employeeService.getActiveEmployeeCount());
    }

    @GetMapping("/reports/average-salary")
    public ResponseEntity<Double> getAverageSalary() {
        return ResponseEntity.ok(employeeService.getAverageSalary());
    }

    @GetMapping("/reports/average-salary/{department}")
    public ResponseEntity<Double> getAverageSalaryByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(employeeService.getAverageSalaryByDepartment(department));
    }

    // Duplicate check endpoints
    @GetMapping("/check/employee-id/{employeeId}")
    public ResponseEntity<Map<String, Object>> checkEmployeeIdExists(@PathVariable String employeeId) {
        boolean exists = employeeService.isEmployeeIdExists(employeeId);
        Map<String, Object> response = Map.of(
            "employeeId", employeeId,
            "exists", exists,
            "message", exists ? "Employee ID already exists" : "Employee ID is available"
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check/email/{email}")
    public ResponseEntity<Map<String, Object>> checkEmailExists(@PathVariable String email) {
        boolean exists = employeeService.isEmailExists(email);
        Map<String, Object> response = Map.of(
            "email", email,
            "exists", exists,
            "message", exists ? "Email already exists" : "Email is available"
        );
        return ResponseEntity.ok(response);
    }
} 