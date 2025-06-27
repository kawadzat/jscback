package io.getarrays.securecapita.products;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.name = :pName")
    Optional<Product> findByName(@Param("pName") String assetDisc);
}
