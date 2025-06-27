package io.getarrays.securecapita.department.dto;

import io.getarrays.securecapita.department.model.DepartmentEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
    private Long id;

    private String code;

    @NotEmpty(message = "name must not be empty.")
    private String name;

    public static DepartmentDto toDto(DepartmentEntity department) {
        if (department == null) {
            return null;
        }
        return new DepartmentDto(department.getId(), department.getCode(), department.getName());
    }
}
