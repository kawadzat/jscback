package io.getarrays.securecapita.itinventory.IT.Controller;

import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.itinventory.IT.Model.ITAssert;
import io.getarrays.securecapita.itinventory.IT.Service.ITAssertServiceInterface;
import io.getarrays.securecapita.itinventory.IT.dto.ITAssertDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/ITAssert")
@RequiredArgsConstructor
public class ITAssertControlller {

    private final ITAssertServiceInterface itAssertService;

    @PostMapping("/create")
    public ResponseEntity<CustomMessage> createITAssert(@RequestBody @Valid ITAssertDto itAssertDto) {
        ITAssert createdITAssert = itAssertService.createITAssert(itAssertDto);
        return ResponseEntity.ok(new CustomMessage("IT Asset created successfully", createdITAssert));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ITAssert> getITAssertById(@PathVariable Long id) {
        ITAssert itAssert = itAssertService.getITAssertById(id);
        return ResponseEntity.ok(itAssert);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ITAssert>> getAllITAsserts() {
        List<ITAssert> itAsserts = itAssertService.getAllITAsserts();
        return ResponseEntity.ok(itAsserts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomMessage> updateITAssert(@PathVariable Long id, @RequestBody @Valid ITAssertDto itAssertDto) {
        ITAssert updatedITAssert = itAssertService.updateITAssert(id, itAssertDto);
        return ResponseEntity.ok(new CustomMessage("IT Asset updated successfully", updatedITAssert));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomMessage> deleteITAssert(@PathVariable Long id) {
        itAssertService.deleteITAssert(id);
        return ResponseEntity.ok(new CustomMessage("IT Asset deleted successfully"));
    }
}
