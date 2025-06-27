package io.getarrays.securecapita.codegenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor
@Table(name = "code_generator")
public class CodeGenerator {
    @Id
    private String type;  // Type of identifier (e.g., "DEPARTMENT", "ORDER", etc.)

    private int currentValue;
}
