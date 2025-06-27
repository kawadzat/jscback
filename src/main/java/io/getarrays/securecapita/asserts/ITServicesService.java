package io.getarrays.securecapita.asserts;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.ITServices;
import io.getarrays.securecapita.asserts.model.Inspection;


public interface ITServicesService {
  //  public Service addServiceToAssertEntity(     AssertEntity assertEntity, String inspection);

    ITServices createITService(ITServices newITServiceS);

    public ITServices addITServicesToAssertEntity(AssertEntity assertEntity, String iTServices);



}
