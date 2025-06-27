package io.getarrays.securecapita.task;

import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(path = "/task")
@RequiredArgsConstructor
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<CustomMessage> createTask(@AuthenticationPrincipal UserDTO currentUser,
                                                    @RequestBody @Valid TaskDto taskDto) throws Exception {
        return ResponseEntity.ok(new CustomMessage("Task Created Successfully", taskService.createTask(currentUser,
                taskDto)));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<CustomMessage> updateTask(@AuthenticationPrincipal UserDTO currentUser,
                                                    @PathVariable Long taskId, @RequestBody @Valid TaskDto taskDto) throws Exception {
        return ResponseEntity.ok(new CustomMessage("Task Updated Successfully", taskService.updateTask(currentUser,
                taskId, taskDto)));

    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long taskId) {
        return ResponseEntity.ok().body(taskService.getTaskById(taskId));
    }

    @GetMapping()
    public ResponseEntity<?> getAllTasks(@AuthenticationPrincipal UserDTO currentUser,
                                         @ModelAttribute TaskSearchDto searchDto) {
        return ResponseEntity.ok().body(taskService.getAllTasks(currentUser, searchDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomMessage> deleteTask(@PathVariable("id") Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok(new CustomMessage("Task deleted successfully"));
    }

    @PatchMapping("/updateStatus/{taskId}")
    public ResponseEntity<CustomMessage> updateTaskStatus(@AuthenticationPrincipal UserDTO currentUser,
                                                          @PathVariable Long taskId, @RequestParam String status) {
        taskService.updateTaskStatus(currentUser, taskId, status);
        return ResponseEntity.ok(new CustomMessage("Task status updated successfully"));
    }

    @GetMapping("/pending-count/{userId}")
    public ResponseEntity<Integer> getPendingTaskCount(@PathVariable Long userId) {
        int count = taskService.getPendingTaskCountForUser(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/completed-count/{userId}")
    public ResponseEntity<Integer> getCompletedTaskCount(@PathVariable Long userId) {
        int count = taskService.getCompletedTaskCountForUser(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/status/report")
    public ResponseEntity<?> getStatusReport(@RequestParam(name = "dateFrom", required = false) @DateTimeFormat(iso =
            DateTimeFormat.ISO.DATE) Date dateFrom,
                                             @RequestParam(name = "dateTo", required = false) @DateTimeFormat(iso =
                                                     DateTimeFormat.ISO.DATE) Date dateTo, @RequestParam(name =
            "userId", required = false) Long userId,
                                             @RequestParam(name = "stationId", required = false) Long stationId,
                                             @RequestParam(name = "departmentId", required = false) Long departmentId) {
        return ResponseEntity.ok().body(taskService.getStatusReport(dateFrom, dateTo, userId, stationId, departmentId));
    }

    @GetMapping("/user-wise-status/report")
    public ResponseEntity<?> getUserWiseStatusReport(@RequestParam(name = "dateFrom", required = false) @DateTimeFormat(iso =
            DateTimeFormat.ISO.DATE) Date dateFrom,
                                             @RequestParam(name = "dateTo", required = false) @DateTimeFormat(iso =
                                                     DateTimeFormat.ISO.DATE) Date dateTo, @RequestParam(name =
            "userId", required = false) Long userId,
                                             @RequestParam(name = "stationId", required = false) Long stationId,
                                             @RequestParam(name = "departmentId", required = false) Long departmentId) {
        return ResponseEntity.ok().body(taskService.getUserWiseStatusReport(dateFrom, dateTo, userId, stationId, departmentId));
    }
}