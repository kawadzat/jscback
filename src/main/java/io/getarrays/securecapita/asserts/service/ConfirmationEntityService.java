package io.getarrays.securecapita.asserts.service;

import io.getarrays.securecapita.asserts.model.ConfirmationEntity;
import io.getarrays.securecapita.asserts.repo.ConfirmationEntityRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfirmationEntityService {

    private  final ConfirmationEntityRepo confirmationEntityRepo;

    public ResponseEntity<?> createConfirmationEntity(ConfirmationEntity newConfirmationEntity) {

        try {
            ConfirmationEntity savedConfirmationEntity = confirmationEntityRepo.save(newConfirmationEntity);
            return ResponseEntity.ok(savedConfirmationEntity);
        } catch (Exception e) {
            // Handle exception, log error, etc.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating confirmation entity");
        }
    }


}
