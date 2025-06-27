package io.getarrays.securecapita.laptoplist;

import io.getarrays.securecapita.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LaptopListRepository   extends JpaRepository<LaptopList, Long>    {

    Optional<LaptopList> findByManufacturerAndModel(String manufacturer, String model);


}
