package io.getarrays.securecapita.repository;

import io.getarrays.securecapita.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository  extends JpaRepository <Stock,Long>{
    //Product findByProductCode(String productCode);
   Stock findByProductCode(String productCode);
    //Optional<Stock> findByProductCode(String productCode);
  //  List<Stock> findByProductCode(String productCode);


}
