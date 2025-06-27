package io.getarrays.securecapita.criminal.service;

import io.getarrays.securecapita.criminal.Repo.CriminalRepo;
import io.getarrays.securecapita.criminal.Domain.Criminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
@Service
public class CriminalService {
    @Autowired
    private CriminalRepo criminalRepo;

    public Optional<Criminal> getCriminalByIdNumber(String idNumber) {
        return Optional.ofNullable(criminalRepo.findByIdNumber(idNumber));
    }

    public Criminal saveCriminal(Criminal criminal) {
        return criminalRepo.save(criminal);
    }

    public void uploadCriminalImage(String idNumber, MultipartFile file) throws IOException {
        Optional<Criminal> criminalOpt = getCriminalByIdNumber(idNumber);
        if (criminalOpt.isPresent()) {
            Criminal criminal = criminalOpt.get();
            criminal.setCriminalImage(file.getBytes());
            criminalRepo.save(criminal);
        } else {
            throw new RuntimeException("Criminal with ID number " + idNumber + " not found.");
        }
    }

    public byte[] getCriminalImageByIdNumber(String idNumber) {
        return getCriminalByIdNumber(idNumber).map(Criminal::getCriminalImage).orElse(null);
    }
}
