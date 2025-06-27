package io.getarrays.securecapita.asserts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssertResponseDto {
    private Long id;
    private String name;
    private String serial;
}
