package io.getarrays.securecapita.asserts.service;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.Inspection;
import io.getarrays.securecapita.asserts.repo.InspectionRepository;
import io.getarrays.securecapita.userlogs.ActionType;
import io.getarrays.securecapita.userlogs.UserLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InspectionService implements io.getarrays.securecapita.asserts.InspectionService {
    private final InspectionRepository inspectionRepository;
    private final UserLogService userLogService;

    /* to create user */
    public Inspection createInspection(Inspection newInspection) {
        Inspection createdInspection = inspectionRepository.save(newInspection);
        userLogService.addLog(ActionType.CREATED,"created inspection with remarks: "+createdInspection.getRemarks());
        return createdInspection;
    }

    @Override
    public Inspection getInspectionById(Long InspectionId) {
        return null;
    }

    @Override
    public Inspection addInspectionToAssertEntity(AssertEntity assertEntity, String inspection) {


        Inspection newInspection = new Inspection();
        newInspection.setDate(new Date());
        newInspection.setRemarks(inspection);
        newInspection.setAssertEntity(assertEntity);


        List<Inspection> inspections = assertEntity.getInspections();
        inspections.add(newInspection);
        userLogService.addLog(ActionType.UPDATED, "added inspection to assert.");
        return newInspection;

    }

    /* updating the user */
    public Inspection updateInspection(Inspection inspection) {

        Inspection updatedInspection = inspectionRepository.save(inspection);
        userLogService.addLog(ActionType.UPDATED, "updated inspection.");

        return updatedInspection;
    }


//    @Override
//    public Inspection addInspectionToAssertEntity(AssertEntity assertEntity, String inspection) {
//        Inspection newInspection = new Inspection();
//        newInspection.setDate(new Date());
//        newInspection.setRemarks(inspection);
//        newInspection.setAssertEntity(assertEntity);
//
//        List<Inspection> inspections = assertEntity.getInspections();
//        inspections.add(newInspection);
//
//           return newInspection;
//    }


}
