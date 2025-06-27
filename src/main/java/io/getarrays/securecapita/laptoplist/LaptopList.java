package io.getarrays.securecapita.laptoplist;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "laptop_list")
public class LaptopList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Manufacturer is required")
    @Size(max = 100, message = "Manufacturer name must not exceed 100 characters")
    @Column(name = "manufacturer", length = 100, nullable = false)
    private String manufacturer;

    @NotBlank(message = "Model is required")
    @Size(max = 100, message = "Model name must not exceed 100 characters")
    @Column(name = "model", length = 100, nullable = false)
    private String model;


}
