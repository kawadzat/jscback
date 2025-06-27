package io.getarrays.securecapita.itinventory;

import io.getarrays.securecapita.antivirus.AntivirusService;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.getarrays.securecapita.antivirus.Antivirus;

@RestController
@RequestMapping(path = "/laptop")
@RequiredArgsConstructor
public class LaptopController {


    @Autowired


  private  AntivirusService antivirusService;

    @Autowired
    private LaptopService laptopService;

    @PostMapping("/create")
    public ResponseEntity<CustomMessage> createLaptop(@AuthenticationPrincipal UserDTO currentUser,
                                                    @RequestBody @Valid LaptopDto laptopDto) throws Exception {
        return ResponseEntity.ok(new CustomMessage("Laptop Created Successfully", laptopService.createLaptop(currentUser,
                laptopDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomMessage> updateLaptop(@AuthenticationPrincipal UserDTO currentUser,
                                                    @PathVariable("id") Long laptopId,
                                                    @RequestBody @Valid LaptopDto laptopDto) {
        return ResponseEntity.ok(new CustomMessage(
                "Laptop Updated Successfully",
                laptopService.updateLaptop(currentUser, laptopId, laptopDto)
        ));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CustomMessage> changeLaptopStatus(@AuthenticationPrincipal UserDTO currentUser,
                                                          @PathVariable("id") Long laptopId,
                                                          @RequestBody LaptopStatus newStatus) {
        return ResponseEntity.ok(new CustomMessage(
                "Laptop Status Updated Successfully",
                laptopService.changeLaptopStatus(currentUser, laptopId, newStatus)
        ));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<LaptopDto>> getAllLaptops(@AuthenticationPrincipal UserDTO currentUser) {
        return ResponseEntity.ok(laptopService.getAllLaptops(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaptopDto> getLaptopById(@AuthenticationPrincipal UserDTO currentUser,
                                                  @PathVariable("id") Long laptopId) {
        return ResponseEntity.ok(laptopService.getLaptopById(currentUser, laptopId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomMessage> deleteLaptop(@AuthenticationPrincipal UserDTO currentUser,
                                                    @PathVariable("id") Long laptopId) {
        laptopService.deleteLaptop(currentUser, laptopId);
        return ResponseEntity.ok(new CustomMessage("Laptop Deleted Successfully"));
    }


    @GetMapping("/serial-number-exists/{serialNumber}")
    public ResponseEntity<Boolean> serialNumberExists(@PathVariable("serialNumber") String serialNumber) {
        return ResponseEntity.ok(laptopService.serialNumberExists(serialNumber));
    }

    @PostMapping("/antivirus/laptop/{id}")
    public ResponseEntity<Antivirus> addAntivirusToLaptop(
            @PathVariable("id") Long laptopId,
            @RequestBody Antivirus antivirus) {
        Antivirus created = antivirusService.addAntivirusToLaptop(laptopId, antivirus);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}/with-maintenance")
    public ResponseEntity<LaptopDto> getLaptopWithMaintenance(@AuthenticationPrincipal UserDTO currentUser, @PathVariable("id") Long laptopId) {
        return ResponseEntity.ok(laptopService.getLaptopById(currentUser, laptopId));
    }
}
