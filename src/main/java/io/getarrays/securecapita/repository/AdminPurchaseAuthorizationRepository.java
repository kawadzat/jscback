package io.getarrays.securecapita.repository;

import io.getarrays.securecapita.domain.AdminPurchaseAuthorization;

import java.util.List;

public interface AdminPurchaseAuthorizationRepository <T extends AdminPurchaseAuthorization>{

    List<T> list();
    T create(T data);

    T get(Long id);

    //
//    void update(T t,Long id);
    boolean delete(Long id);
}
