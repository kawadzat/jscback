package io.getarrays.securecapita.PurchaseRequest.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.getarrays.securecapita.PurchaseRequest.enums.PurchaseRequestStatusEnum;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.itauditing.Auditable;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Builder
@Entity
@NoArgsConstructor
@Table(name = "purchase_request_approval_history")
public class PurchaseRequestApprovalHistory extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty
    private Long id;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "purchase_request_id", nullable = false)
    private PurchaseRequestEntity purchaseRequest;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    @Nonnull
    private PurchaseRequestStatusEnum status;

    @Column(columnDefinition = "longblob")
    private String signature;
}
