package io.getarrays.securecapita.hr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // Find by employee ID
    Optional<Employee> findByEmployeeId(String employeeId);
    boolean existsByEmployeeId(String employeeId);
    
    // Find by email
    Optional<Employee> findByEmail(String email);
    boolean existsByEmail(String email);
    
    // Find by status
    List<Employee> findByStatus(EmployeeStatus status);
    
    // Find by department
    List<Employee> findByDepartment(String department);
    
    // Find by job title
    List<Employee> findByJobTitle(String jobTitle);
    
    // Find by manager
    List<Employee> findByManagerId(Long managerId);
    
    // Find by name (first or last)
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    
    // Find active employees
    List<Employee> findByStatusAndTerminationDateIsNull(EmployeeStatus status);
    
    // Find employees hired in date range
    List<Employee> findByHireDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find employees by salary range
    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :minSalary AND :maxSalary")
    List<Employee> findBySalaryRange(@Param("minSalary") Double minSalary, @Param("maxSalary") Double maxSalary);
    
    // Find employees by department and status
    List<Employee> findByDepartmentAndStatus(String department, EmployeeStatus status);
    
    // Count employees by department
    @Query("SELECT e.department, COUNT(e) FROM Employee e GROUP BY e.department")
    List<Object[]> countEmployeesByDepartment();
} 