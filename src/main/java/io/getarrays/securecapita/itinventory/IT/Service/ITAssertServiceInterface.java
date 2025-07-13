package io.getarrays.securecapita.itinventory.IT.Service;

import io.getarrays.securecapita.itinventory.IT.Model.ITAssert;
import io.getarrays.securecapita.itinventory.IT.Model.ITAssertMatenance;
import io.getarrays.securecapita.itinventory.IT.dto.ITAssertDto;

import java.util.List;

public interface ITAssertServiceInterface {

    ITAssert createITAssert(ITAssertDto itAssertDto);
    
    ITAssert getITAssertById(Long id);
    
    List<ITAssert> getAllITAsserts();
    
    ITAssert updateITAssert(Long id, ITAssertDto itAssertDto);
    
    void deleteITAssert(Long id);

    void addITAssertMatenanceToITAssert(Long id, ITAssertMatenance itAssertMatenance);

}
