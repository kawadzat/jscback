package io.getarrays.securecapita.productsrequests.dto;

import io.getarrays.securecapita.productsrequests.entity.ProductRequestItemEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestItemDto {
    private Long id;
    @NotNull(message = "missing itemNumber.")
    @NotEmpty(message = "empty itemNumber.")
    private int itemNumber;
    @NotNull(message = "missing ItemDescription.")
    @NotEmpty(message = "empty ItemDescription.")
    private String ItemDescription;
    @NotNull(message = "missing unitPrice.")
    @NotEmpty(message = "empty unitPrice.")
    private String unitPrice;
    @NotNull(message = "missing estimateValue.")
    @NotEmpty(message = "empty estimateValue.")
    private String estimateValue;
    @NotNull(message = "missing quantity.")
    @NotEmpty(message = "empty quantity.")
    private int quantity;

    public static List<ProductRequestItemEntity> fromDtoList(List<ProductRequestItemDto> dtos) {
        List<ProductRequestItemEntity> entities = new ArrayList<>();
        for (ProductRequestItemDto dto : dtos) {
            ProductRequestItemEntity entity = new ProductRequestItemEntity();
            entity.setItemNumber(dto.getItemNumber());
            entity.setItemDescription(dto.getItemDescription());
            entity.setUnitPrice(dto.getUnitPrice());
            entity.setEstimateValue(dto.getEstimateValue());
            entity.setQuantity(dto.getQuantity());
            entities.add(entity);
        }
        return entities;
    }

    public static List<ProductRequestItemDto> fromEntityList(List<ProductRequestItemEntity> entities) {
        List<ProductRequestItemDto> dtoList = new ArrayList<>();
        for (ProductRequestItemEntity entity : entities) {
            ProductRequestItemDto dto1 = new ProductRequestItemDto();
            dto1.setId(entity.getId());
            dto1.setItemNumber(entity.getItemNumber());
            dto1.setItemDescription(entity.getItemDescription());
            dto1.setUnitPrice(entity.getUnitPrice());
            dto1.setEstimateValue(entity.getEstimateValue());
            dto1.setQuantity(entity.getQuantity());
            dtoList.add(dto1);
        }
        return dtoList;
    }
}
