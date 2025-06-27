package io.getarrays.securecapita.asserts.controller;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.ITServices;
import io.getarrays.securecapita.asserts.model.Inspection;
import io.getarrays.securecapita.asserts.service.AssertService;
import io.getarrays.securecapita.asserts.service.ITServicesService;
import io.getarrays.securecapita.jasper.pdf.JasperPdfService;
import io.getarrays.securecapita.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalTime.now;

@RestController
@RequestMapping(path = "/itservice")
@RequiredArgsConstructor
public class ITServicesController {
    private JasperPdfService jasperPdfService;
    private final UserService userService;
    private final ITServicesService iTServicesService;

    private final AssertService assertEntityService;
    @PostMapping("/create")
    public ITServices createITServices(@RequestBody ITServices newITServices) {
        ITServices createdITServices =   iTServicesService.createITService(newITServices)  ;
        return createdITServices   ;
    }

    @PostMapping("/itservice/{id}")
    public ResponseEntity<Map<String, Object>> addITServiceToAssertEnity(@PathVariable("id") AssertEntity id, @RequestBody ITServices itServices) {
        iTServicesService.addITServicesToAssertEntity(id, String.valueOf(itServices));
        List<AssertEntity> asserts = assertEntityService.getAsserts();

        Map<String, Object> response = new HashMap<>();
        response.put("timeStamp", now().toString());
        response.put("data", asserts);
        response.put("message", String.format("itservice    added to added with ID: %s", id));

        return ResponseEntity.ok()
                .body(response);
    }

}
