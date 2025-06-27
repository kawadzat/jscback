package io.getarrays.securecapita.itinventory;

import io.getarrays.securecapita.task.Task;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LaptopRepository extends JpaRepository<Laptop, Long> {


    boolean existsBySerialNumber(String serialNumber);



}
