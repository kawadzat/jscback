package io.getarrays.securecapita.hr;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface EmployeeService {
    
    // Basic CRUD operations
    Employee create(Employee employee);
    Employee update(Employee employee);
    Employee getById(Long id);
    Employee getByEmployeeId(String employeeId);
    Employee getByEmail(String email);
    List<Employee> getAll();
    void delete(Long id);
    
    // Duplicate prevention
    boolean isEmployeeIdExists(String employeeId);
    boolean isEmailExists(String email);
    Employee createWithDuplicateCheck(Employee employee);
    Employee updateWithDuplicateCheck(Long id, Employee employee);
    
    // Search and filter methods
    List<Employee> getByStatus(EmployeeStatus status);
    List<Employee> getByDepartment(String department);
    List<Employee> getByJobTitle(String jobTitle);
    List<Employee> getByManager(Long managerId);
    List<Employee> searchByName(String name);
    List<Employee> getActiveEmployees();
    List<Employee> getEmployeesHiredBetween(LocalDate startDate, LocalDate endDate);
    List<Employee> getEmployeesBySalaryRange(Double minSalary, Double maxSalary);
    List<Employee> getEmployeesByDepartmentAndStatus(String department, EmployeeStatus status);
    
    // Business logic methods
    Employee terminateEmployee(Long id, String reason);
    Employee activateEmployee(Long id);
    Employee deactivateEmployee(Long id);
    Employee changeDepartment(Long id, String newDepartment);
    Employee changeJobTitle(Long id, String newJobTitle);
    Employee updateSalary(Long id, Double newSalary);
    
    // Reporting methods
    Map<String, Long> getEmployeeCountByDepartment();
    Long getTotalEmployeeCount();
    Long getActiveEmployeeCount();
    Double getAverageSalary();
    Double getAverageSalaryByDepartment(String department);
} 