package io.getarrays.securecapita.procurement;

import org.springframework.web.multipart.MultipartFile;


public interface MinutesRepository {
    MinutesEntity saveFile(MultipartFile file, MinutesEntity pEntity);
//    int save(PurchaseRequestEntity purchaseRequest);
//
//    int update(PurchaseRequestEntity purchaseRequest);
//
//    Optional<PurchaseRequestEntity> findById(Long id);
//
//    int deleteById(Long id);
//
//    List<PurchaseRequestEntity> findAll();
//
//    Stream<PurchaseRequestEntity> findAll(io.getarrays.securecapita.
//                                                  purchaserequest.Page page);





}
