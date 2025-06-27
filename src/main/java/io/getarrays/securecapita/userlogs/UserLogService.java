package io.getarrays.securecapita.userlogs;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class UserLogService {
    private final UserLogsRepository userLogsRepository;
    private final UserRepository1 userRepository1;
    //get all user logs
    public ResponseEntity<Object> getUserLogs(int page,int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return ResponseEntity.ok(userLogsRepository.findAllUserLogs(pageable));
    }

    //add log
    //where do you want to add log

    public void addLog(ActionType actionType, String description){
        UserDTO user= (UserDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserLog userLog=UserLog.builder()
                .actionType(actionType)
                .user(userRepository1.findById(user.getId()).get())
                .description(description)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        userLogsRepository.save(userLog);
    }
}
