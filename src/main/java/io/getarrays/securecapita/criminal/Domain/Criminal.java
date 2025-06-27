package io.getarrays.securecapita.criminal.Domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Entity
@Table(name = "criminal")
public class Criminal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String fullName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate dateOfBirth;

    @Column(unique = true, nullable = false)
    private String idNumber;  // Unique national ID
    private String country;
    private String gender;
    private String offence;
    @Lob
    private byte[] criminalImage;  // Store image as byte[]

    // Getters and Setters
    public byte[] getCriminalImage() { return criminalImage; }
    public void setCriminalImage(byte[] criminalImage) { this.criminalImage = criminalImage; }


}
