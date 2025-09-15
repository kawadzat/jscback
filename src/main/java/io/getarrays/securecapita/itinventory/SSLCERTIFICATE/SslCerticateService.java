package io.getarrays.securecapita.itinventory.SSLCERTIFICATE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SslCerticateService {
    
    @Autowired
    private SslCerticateRepository sslCerticateRepository;

    public long countSslCertificates() {
        return sslCerticateRepository.count();
    }

    public SslCerticateDto create(SslCerticateDto dto) {
        SslCerticate entity = mapToEntity(dto);
        SslCerticate saved = sslCerticateRepository.save(entity);
        return mapToDto(saved);
    }

    public List<SslCerticateDto> findAll() {
        return sslCerticateRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public SslCerticateDto findById(Long id) {
        SslCerticate entity = sslCerticateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SSL Certificate not found with id: " + id));
        return mapToDto(entity);
    }

    public SslCerticateDto update(Long id, SslCerticateDto dto) {
        SslCerticate existing = sslCerticateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SSL Certificate not found with id: " + id));

        existing.setPurchaseDate(dto.getPurchaseDate());
        existing.setPlan(dto.getPlan());
        existing.setValidity(dto.getValidity());
        existing.setVendor(dto.getVendor());
        existing.setSupplier(dto.getSupplier());
        existing.setExpiryDate(dto.getExpiryDate());
        existing.setDomainName(dto.getDomainName());
        existing.setCertificateSerialNumber(dto.getCertificateSerialNumber());
        existing.setNotes(dto.getNotes());

        SslCerticate saved = sslCerticateRepository.save(existing);
        return mapToDto(saved);
    }

    public void delete(Long id) {
        if (!sslCerticateRepository.existsById(id)) {
            throw new RuntimeException("SSL Certificate not found with id: " + id);
        }
        sslCerticateRepository.deleteById(id);
    }

    private SslCerticateDto mapToDto(SslCerticate entity) {
        SslCerticateDto dto = new SslCerticateDto();
        dto.setId(entity.getId());
        dto.setPurchaseDate(entity.getPurchaseDate());
        dto.setPlan(entity.getPlan());
        dto.setValidity(entity.getValidity());
        dto.setVendor(entity.getVendor());
        dto.setSupplier(entity.getSupplier());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setDomainName(entity.getDomainName());
        dto.setCertificateSerialNumber(entity.getCertificateSerialNumber());
        dto.setNotes(entity.getNotes());
        return dto;
    }

    private SslCerticate mapToEntity(SslCerticateDto dto) {
        SslCerticate entity = new SslCerticate();
        entity.setPurchaseDate(dto.getPurchaseDate());
        entity.setPlan(dto.getPlan());
        entity.setValidity(dto.getValidity());
        entity.setVendor(dto.getVendor());
        entity.setSupplier(dto.getSupplier());
        entity.setExpiryDate(dto.getExpiryDate());
        entity.setDomainName(dto.getDomainName());
        entity.setCertificateSerialNumber(dto.getCertificateSerialNumber());
        entity.setNotes(dto.getNotes());
        return entity;
    }
}
