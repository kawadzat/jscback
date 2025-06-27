package io.getarrays.securecapita.productsrequests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductrequestResponsePage {
    private int size;
    private int page;
    private boolean next;
    private boolean previous;
    private Long total;
    private List<ProductRequestDto> requests;
}
