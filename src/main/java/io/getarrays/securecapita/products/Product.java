package io.getarrays.securecapita.products;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "assert_products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}

//how are w