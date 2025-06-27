package io.getarrays.securecapita.asserts.controller;

import io.getarrays.securecapita.asserts.InspectionService;
import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.Inspection;
import io.getarrays.securecapita.asserts.service.AssertService;
import io.getarrays.securecapita.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalTime.now;

@RestController
@RequestMapping(path = "/inspection")
@RequiredArgsConstructor
public class InspectionController {
   private final InspectionService inspectionService;
    private final UserService userService;

    private final AssertService assertEntityService;



    @PostMapping("/create")
    public Inspection createStation(@RequestBody Inspection newInspection) {
        Inspection createdInspection = inspectionService.createInspection(newInspection);
        return createdInspection;
    }

//it is dynamic already

    @PostMapping("/inspection/{id}")
    public ResponseEntity<Map<String, Object>> addInspectionToAssertEnity(@PathVariable("id") AssertEntity id, @RequestBody Inspection inspection) {
        inspectionService.addInspectionToAssertEntity(id, String.valueOf(inspection));
        List<AssertEntity> asserts = assertEntityService.getAsserts();

        Map<String, Object> response = new HashMap<>();
        response.put("timeStamp", now().toString());
        response.put("data", asserts);
        response.put("message", String.format("Invoice added to customer with ID: %s", id));

        return ResponseEntity.ok()
                .body(response);
    }



    @PutMapping("/update/{id}")
    public Inspection updateInspection(@PathVariable("id") Long userId  ,@RequestBody  Inspection inspection) {

        Inspection oldInspection =inspectionService.getInspectionById(userId);

         oldInspection.setDate(inspection.getDate());
        oldInspection.setRemarks(inspection.getRemarks());
        Inspection   updatedInspection =inspectionService.updateInspection(oldInspection);


        return updatedInspection;
    }



//    @PostMapping("/{assertEntityId}/inspections")
//    public ResponseEntity<Inspection> addInspectionToAssertEntity(
//            @PathVariable Long assertEntityId,
//            @RequestBody String inspectionData) {
//        System.out.println(assertEntityId + " this " + inspectionData);
//
//        // Retrieve or create the AssertEntity based on the assertEntityId
//        AssertEntity assertEntity = // code to retrieve or create the AssertEntity
//
//                // Call the service method to add the inspection to the assert entity
//
//        Inspection newInspection = inspectionService.addInspectionToAssertEntity(assertEntity, inspectionData);
//
//        if (newInspection == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//        return ResponseEntity.ok(newInspection);
//    }










    }















