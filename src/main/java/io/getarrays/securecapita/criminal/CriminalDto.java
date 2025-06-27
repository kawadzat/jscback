package io.getarrays.securecapita.criminal;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
@Getter
@Setter
public class CriminalDto {

    private Long id;
    private String FullName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Timestamp dateOfBirth;
    private String Country;
    private String gender ;
    private String Offence;

}
