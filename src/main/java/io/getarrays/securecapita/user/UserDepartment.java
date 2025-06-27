package io.getarrays.securecapita.user;

import io.getarrays.securecapita.department.model.DepartmentEntity;
import io.getarrays.securecapita.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_departments")
public class UserDepartment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private User assignedBy;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    private Timestamp createdDate;
}
