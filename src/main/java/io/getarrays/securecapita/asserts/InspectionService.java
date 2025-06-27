package io.getarrays.securecapita.asserts;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.Inspection;
public interface InspectionService {



    public Inspection addInspectionToAssertEntity(AssertEntity assertEntity, String inspection);

    Inspection createInspection(Inspection newInspection);

    Inspection getInspectionById(Long userId);
    Inspection updateInspection(Inspection inspection);
}
