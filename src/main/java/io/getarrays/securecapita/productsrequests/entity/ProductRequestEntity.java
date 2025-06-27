package io.getarrays.securecapita.productsrequests.entity;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.productsrequests.enums.ProductRequestStatus;
import io.getarrays.securecapita.roles.prerunner.AUTH_ROLE;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "product_requests")
public class ProductRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private ProductRequestStatus status;
    @ManyToOne
    private User initiator;
    @Enumerated(value = EnumType.STRING)
    private AUTH_ROLE roleStage;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<ProductRequestItemEntity> products;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
