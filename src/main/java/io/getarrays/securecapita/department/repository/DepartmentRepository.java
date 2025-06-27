package io.getarrays.securecapita.department.repository;

import io.getarrays.securecapita.department.model.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    boolean existsByName(String name);
}
