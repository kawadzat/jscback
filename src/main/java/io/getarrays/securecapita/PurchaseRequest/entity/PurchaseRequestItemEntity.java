package io.getarrays.securecapita.PurchaseRequest.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity
@Table(name = "purchase_request_item")
public class PurchaseRequestItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private int itemNumber;

    private String ref;

    private String ItemDescription;

    private Double unitPrice;

    private Double estimatedValue;

    private Double quantity;

    @ManyToOne
    @JoinColumn(name = "purchase_request_id", nullable = false)
    private PurchaseRequestEntity purchaseRequest;

}
