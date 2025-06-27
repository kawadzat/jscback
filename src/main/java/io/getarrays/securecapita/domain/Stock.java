package io.getarrays.securecapita.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import static jakarta.persistence.GenerationType.AUTO;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = "product_code"))

public class Stock {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP")
    private Date date;


    private Long productId;
    @Column(name = "product_name", nullable = false)
    private String productName;

    /*@Temporal(TemporalType.TIMESTAMP)*/

    @Column(name="product_code", unique = false)
    private String productCode;

    private int quantity;


}
