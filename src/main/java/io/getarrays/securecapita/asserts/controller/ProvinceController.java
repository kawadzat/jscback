package io.getarrays.securecapita.asserts.controller;

import io.getarrays.securecapita.asserts.model.Province;
import io.getarrays.securecapita.asserts.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/provinces")
@RequiredArgsConstructor
public class ProvinceController {
    private final ProvinceService provinceService;

    @GetMapping("/all")
    public ResponseEntity<List<Province>> getAllProvinces() {
        return provinceService.getAllProvinces();
    }
}
