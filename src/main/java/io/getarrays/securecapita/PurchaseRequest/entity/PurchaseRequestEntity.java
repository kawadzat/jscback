package io.getarrays.securecapita.PurchaseRequest.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.getarrays.securecapita.PurchaseRequest.enums.PurchaseRequestStatusEnum;
import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.department.model.DepartmentEntity;
import io.getarrays.securecapita.itauditing.Auditable;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
@Table(name = "purchase_request")
public class PurchaseRequestEntity extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty
    private Long id;

    private String code;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    private String reason;

    @OneToMany(mappedBy = "purchaseRequest", cascade = CascadeType.ALL)
    private List<PurchaseRequestItemEntity> requestItems;

    @OneToMany(mappedBy = "purchaseRequest", cascade = CascadeType.ALL)
    private List<PurchaseRequestApprovalHistory> approvalHistories;

    @Column
    @Enumerated(EnumType.STRING)
    @Nonnull
    private PurchaseRequestStatusEnum status;
}
