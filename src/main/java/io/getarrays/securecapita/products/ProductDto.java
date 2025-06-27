package io.getarrays.securecapita.products;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;

    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be null")
    @Size(min = 2, max = 25, message = "name size should be min 2 and max 25.")
    private String name;

    private String description;


    public Product toEntity() {
        return Product.builder().name(name).description(description).build();
    }

    public static ProductDto toDto(Product product) {
        return ProductDto.builder().id(product.getId()).name(product.getName()).description(product.getDescription()).build();
    }

    public static List<ProductDto> toDtos(List<Product> all) {
        return all.stream().map(ProductDto::toDto).collect(Collectors.toList());
    }




}
