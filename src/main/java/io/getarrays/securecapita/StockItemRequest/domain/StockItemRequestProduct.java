package io.getarrays.securecapita.StockItemRequest.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.asserts.model.AssertEntity;

import jakarta.persistence.*;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Builder
@Entity
@NoArgsConstructor
public class StockItemRequestProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numberItem;
    private String description;
    private int unitPrice;
    private String estimateValue;
    private int quantity;
//    @ManyToOne
//    @JoinColumn(name = "StockItemRequest_id")
//    @JsonBackReference
//    private  StockItemRequest stockItemRequest;

    @ManyToOne
    @JoinColumn(name = "stock_item_request_id")
    private StockItemRequest stockItemRequest;
}
