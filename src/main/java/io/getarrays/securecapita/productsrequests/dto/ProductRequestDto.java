package io.getarrays.securecapita.productsrequests.dto;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.productsrequests.entity.ProductRequestEntity;
import io.getarrays.securecapita.productsrequests.entity.ProductRequestItemEntity;
import io.getarrays.securecapita.productsrequests.enums.ProductRequestStatus;
import io.getarrays.securecapita.roles.prerunner.AUTH_ROLE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    private Long id;
    private String status;
    private String initiator;
    private String roleStage;
    private boolean created;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private List<ProductRequestItemDto> products;


    public static ProductRequestDto fromEntity(ProductRequestEntity product, UserDTO user) {
        return ProductRequestDto.builder()
                .id(product.getId())
                .status(product.getStatus().name())
                .initiator(product.getInitiator().getEmail() + "-" + product.getInitiator().getRoles().toString())
                .roleStage(product.getRoleStage().name())
                .created(product.getInitiator().getId().equals(user.getId()))
                .createdDate(product.getCreatedDate())
                .updatedDate(product.getUpdatedDate())
                .products(ProductRequestItemDto.fromEntityList(product.getProducts()))
                .build();
    }

    public static List<ProductRequestDto> fromEntities(List<ProductRequestEntity> products) {
        List<ProductRequestDto> productDtos = new ArrayList<>();
        for (ProductRequestEntity product : products) {
            UserDTO user = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ProductRequestDto productDto = fromEntity(product, user); // Utilizing the existing fromEntity method
            productDtos.add(productDto);
        }
        return productDtos;
    }

    public ProductRequestEntity toEntity() {
        return ProductRequestEntity.builder()
                .id(this.getId())
                .build();
    }
}
