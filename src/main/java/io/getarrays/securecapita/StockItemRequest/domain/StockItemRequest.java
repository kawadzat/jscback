package io.getarrays.securecapita.StockItemRequest.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.getarrays.securecapita.StockItemRequest.enums.StockItemRequestStatus;
import io.getarrays.securecapita.asserts.model.Inspection;
import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.itauditing.Auditable;
import io.getarrays.securecapita.productsrequests.entity.ProductRequestItemEntity;
import io.getarrays.securecapita.productsrequests.enums.ProductRequestStatus;
import io.getarrays.securecapita.roles.prerunner.AUTH_ROLE;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;


@Getter
@Setter
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Builder
@Entity
@NoArgsConstructor
public class StockItemRequest  extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date date;
    private String departmentCode;
    private String receiverEmail;
    private String reasonItem;

    @Enumerated(value = EnumType.STRING)
    private StockItemRequestStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<StockItemRequestProduct> products;


    @ManyToOne
    private User initiator;
    @Enumerated(value = EnumType.STRING)
    private AUTH_ROLE roleStage;



    private Timestamp createdDate;
    private Timestamp updatedDate;







//    @OneToMany(mappedBy = "assertEntity")
//    @JsonManagedReference
//    private List<StockItemRequestProduct> stockItemRequestProducts;

    @OneToMany(mappedBy = "stockItemRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockItemRequestProduct> stockItemRequestProducts = new ArrayList<>();


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "station_id")
    private Station station;

    @Transient
    @NotNull
    private Long selectedStationID;

}
