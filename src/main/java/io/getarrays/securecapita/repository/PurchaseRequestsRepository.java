package io.getarrays.securecapita.repository;

import io.getarrays.securecapita.domain.PurchaseRequisition;

import java.util.List;

public interface PurchaseRequestsRepository <T extends PurchaseRequisition>{
     List<T> list();
    T create(T data);

    T get(Long id);

//
//    void update(T t,Long id);
    boolean delete(Long id);


}
