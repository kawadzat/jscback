package io.getarrays.securecapita.department.prerunner;

import io.getarrays.securecapita.department.model.DepartmentEntity;
import io.getarrays.securecapita.department.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentPreRunner implements CommandLineRunner {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeDepartments();
    }

    private void initializeDepartments() {
        List<DepartmentEntity> departments = new ArrayList<>();

        addDepartmentIfNotExists(departments, "HR", "Human Resources");
        addDepartmentIfNotExists(departments, "IT", "Information Technology");
        addDepartmentIfNotExists(departments, "FIN", "Finance");
        addDepartmentIfNotExists(departments, "CMM", "Communication");
        addDepartmentIfNotExists(departments, "OPS", "Operations");
        addDepartmentIfNotExists(departments, "PRT", "Protocol");

        if (!departments.isEmpty()) {
            departmentRepository.saveAll(departments);
        }
    }

    private void addDepartmentIfNotExists(List<DepartmentEntity> departments, String shortCode, String name) {
        if (!departmentRepository.existsByName(name)) {
            departments.add(createDepartment(shortCode, name));
        }
    }

    private DepartmentEntity createDepartment(String shortCode, String name) {
        DepartmentEntity department = new DepartmentEntity();
        department.setCode(shortCode);
        department.setName(name);
        return department;
    }
}