package io.getarrays.securecapita.hr;

import io.getarrays.securecapita.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        // Set default status if not provided
        if (employee.getStatus() == null) {
            employee.setStatus(EmployeeStatus.ACTIVE);
        }
        
        // Set hire date if not provided
        if (employee.getHireDate() == null) {
            employee.setHireDate(LocalDate.now());
        }
        
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        if (!employeeRepository.existsById(employee.getId())) {
            throw new ResourceNotFoundException("Employee not found with id: " + employee.getId());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getById(Long id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public Employee getByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with employee ID: " + employeeId));
    }

    @Override
    public Employee getByEmail(String email) {
        return employeeRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
    }

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public boolean isEmployeeIdExists(String employeeId) {
        return employeeRepository.existsByEmployeeId(employeeId);
    }

    @Override
    public boolean isEmailExists(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Override
    public Employee createWithDuplicateCheck(Employee employee) {
        // Check for duplicate employee ID
        if (employeeRepository.existsByEmployeeId(employee.getEmployeeId())) {
            throw new IllegalArgumentException("Employee with ID '" + employee.getEmployeeId() + "' already exists");
        }
        
        // Check for duplicate email
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Employee with email '" + employee.getEmail() + "' already exists");
        }
        
        return create(employee);
    }

    @Override
    public Employee updateWithDuplicateCheck(Long id, Employee employee) {
        Employee existingEmployee = getById(id);
        
        // Check if new employee ID conflicts with another employee
        if (!existingEmployee.getEmployeeId().equals(employee.getEmployeeId()) && 
            employeeRepository.existsByEmployeeId(employee.getEmployeeId())) {
            throw new IllegalArgumentException("Employee with ID '" + employee.getEmployeeId() + "' already exists");
        }
        
        // Check if new email conflicts with another employee
        if (!existingEmployee.getEmail().equals(employee.getEmail()) && 
            employeeRepository.existsByEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Employee with email '" + employee.getEmail() + "' already exists");
        }
        
        employee.setId(id);
        return update(employee);
    }

    @Override
    public List<Employee> getByStatus(EmployeeStatus status) {
        return employeeRepository.findByStatus(status);
    }

    @Override
    public List<Employee> getByDepartment(String department) {
        return employeeRepository.findByDepartment(department);
    }

    @Override
    public List<Employee> getByJobTitle(String jobTitle) {
        return employeeRepository.findByJobTitle(jobTitle);
    }

    @Override
    public List<Employee> getByManager(Long managerId) {
        return employeeRepository.findByManagerId(managerId);
    }

    @Override
    public List<Employee> searchByName(String name) {
        return employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    @Override
    public List<Employee> getActiveEmployees() {
        return employeeRepository.findByStatus(EmployeeStatus.ACTIVE);
    }

    @Override
    public List<Employee> getEmployeesHiredBetween(LocalDate startDate, LocalDate endDate) {
        return employeeRepository.findByHireDateBetween(startDate, endDate);
    }

    @Override
    public List<Employee> getEmployeesBySalaryRange(Double minSalary, Double maxSalary) {
        return employeeRepository.findBySalaryRange(minSalary, maxSalary);
    }

    @Override
    public List<Employee> getEmployeesByDepartmentAndStatus(String department, EmployeeStatus status) {
        return employeeRepository.findByDepartmentAndStatus(department, status);
    }

    @Override
    public Employee terminateEmployee(Long id, String reason) {
        Employee employee = getById(id);
        employee.setStatus(EmployeeStatus.TERMINATED);
        employee.setTerminationDate(LocalDate.now());
        employee.setTerminationReason(reason);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee activateEmployee(Long id) {
        Employee employee = getById(id);
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setTerminationDate(null);
        employee.setTerminationReason(null);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee deactivateEmployee(Long id) {
        Employee employee = getById(id);
        employee.setStatus(EmployeeStatus.INACTIVE);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee changeDepartment(Long id, String newDepartment) {
        Employee employee = getById(id);
        employee.setDepartment(newDepartment);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee changeJobTitle(Long id, String newJobTitle) {
        Employee employee = getById(id);
        employee.setJobTitle(newJobTitle);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateSalary(Long id, Double newSalary) {
        Employee employee = getById(id);
        employee.setSalary(newSalary);
        return employeeRepository.save(employee);
    }

    @Override
    public Map<String, Long> getEmployeeCountByDepartment() {
        List<Object[]> results = employeeRepository.countEmployeesByDepartment();
        Map<String, Long> departmentCounts = new HashMap<>();
        
        for (Object[] result : results) {
            String department = (String) result[0];
            Long count = (Long) result[1];
            departmentCounts.put(department, count);
        }
        
        return departmentCounts;
    }

    @Override
    public Long getTotalEmployeeCount() {
        return employeeRepository.count();
    }

    @Override
    public Long getActiveEmployeeCount() {
        return (long) employeeRepository.findByStatus(EmployeeStatus.ACTIVE).size();
    }

    @Override
    public Double getAverageSalary() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
            .filter(e -> e.getSalary() != null)
            .mapToDouble(Employee::getSalary)
            .average()
            .orElse(0.0);
    }

    @Override
    public Double getAverageSalaryByDepartment(String department) {
        List<Employee> employees = employeeRepository.findByDepartment(department);
        return employees.stream()
            .filter(e -> e.getSalary() != null)
            .mapToDouble(Employee::getSalary)
            .average()
            .orElse(0.0);
    }
} 