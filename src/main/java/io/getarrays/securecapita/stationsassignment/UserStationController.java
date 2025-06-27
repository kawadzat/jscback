package io.getarrays.securecapita.stationsassignment;

import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/station")
public class UserStationController {
    private final UserStationService userStationService;
    @GetMapping("/get")
    public ResponseEntity<?> getStations(){
        return userStationService.getCurrentUserStations();
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getUserStations(@RequestParam("userId")Long userId){
        return userStationService.getUserStations(userId);
    }

    @GetMapping("/getCount")
    public ResponseEntity<?> getUserStationsCount(){
        return ResponseEntity.ok(userStationService.getUserStationsCount());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStationToUser(@RequestParam(name = "userId") Long userId,@RequestParam(name = "stationId")Long stationId){
        return userStationService.addStationToUser(userId,stationId);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeStationToUser(@RequestParam(name = "userId") Long userId, @RequestParam(name = "stationId")Long stationId){
        return userStationService.removeStationFromUser(userId, stationId);
    }
}
