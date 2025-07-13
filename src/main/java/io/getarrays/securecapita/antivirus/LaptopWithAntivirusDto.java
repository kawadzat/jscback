package io.getarrays.securecapita.antivirus;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.antivirus.AntivirusDto;
import io.getarrays.securecapita.itinventory.LaptopDto;
import lombok.*;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
public class LaptopWithAntivirusDto {
    
    private LaptopDto laptop;
    private List<AntivirusDto> antivirusList;
    private Integer antivirusCount;
    
    public LaptopWithAntivirusDto(LaptopDto laptop, List<AntivirusDto> antivirusList) {
        this.laptop = laptop;
        this.antivirusList = antivirusList;
        this.antivirusCount = antivirusList != null ? antivirusList.size() : 0;
    }
} 