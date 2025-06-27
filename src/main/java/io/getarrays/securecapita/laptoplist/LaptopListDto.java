package io.getarrays.securecapita.laptoplist;

import io.getarrays.securecapita.itinventory.LaptopDto;
import io.getarrays.securecapita.products.Product;
import io.getarrays.securecapita.products.ProductDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class LaptopListDto {

    private Long id;

    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be null")
    @Size(min = 2, max = 25, message = "name size should be min 2 and max 25.")
    private String manufacturer;

    private String model;

    public  LaptopList toEntity() {
        return  LaptopList.builder().manufacturer(manufacturer).model(model).build();
    }

    public static  LaptopListDto toDto( LaptopList laptopList) {
        return LaptopListDto .builder().id(laptopList.getId()).manufacturer(laptopList.getManufacturer()).model(laptopList.getModel()).build();
    }

}
