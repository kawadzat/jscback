package io.getarrays.securecapita.asserts.service;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.ITServices;
import io.getarrays.securecapita.asserts.model.Inspection;
import io.getarrays.securecapita.asserts.repo.ITServicesRepository;
import io.getarrays.securecapita.userlogs.ActionType;
import io.getarrays.securecapita.userlogs.UserLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ITServicesService implements io.getarrays.securecapita.asserts.ITServicesService {

    private final UserLogService userLogService;

    private final ITServicesRepository itServicesRepository;


    @Override
    public io.getarrays.securecapita.asserts.model.ITServices createITService(io.getarrays.securecapita.asserts.model.ITServices newITServiceS) {
        io.getarrays.securecapita.asserts.model.ITServices createdITServices = itServicesRepository.save(newITServiceS);
        userLogService.addLog(ActionType.CREATED,"created inspection with remarks: "+ createdITServices .getRemarks());
        return  createdITServices ;
    }

    @Override
    public ITServices addITServicesToAssertEntity(AssertEntity assertEntity, String iTServices) {

        ITServices      newITServices = new ITServices();
        newITServices.setDate(new Date());
        newITServices.setRemarks(iTServices);
      newITServices.setAssertEntity(assertEntity);

        List<ITServices> iTServicess  = assertEntity.getItServices();
        iTServicess.add(newITServices);
        userLogService.addLog(ActionType.UPDATED, "added itservice to assert.");
        return newITServices;
    }
}
