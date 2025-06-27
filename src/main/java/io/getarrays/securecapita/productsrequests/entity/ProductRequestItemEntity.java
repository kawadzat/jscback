package io.getarrays.securecapita.productsrequests.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "request_items")
public class ProductRequestItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty
    private Long id;
    private int itemNumber;
    private String ItemDescription;
    private String unitPrice;
    private String estimateValue;
    private int quantity;
}
