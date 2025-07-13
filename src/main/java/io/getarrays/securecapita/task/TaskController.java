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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/task")
@RequiredArgsConstructor
public class TaskController {
    @Autowired
    private TaskService taskService;
    
    private final RepetitiveTaskReminderService repetitiveTaskReminderService;
    private final TaskRepository taskRepository;

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

    // Task Type endpoints
    @GetMapping("/types")
    public ResponseEntity<?> getAllTaskTypes() {
        return ResponseEntity.ok(TaskTypeUtils.getAllTaskTypes());
    }

    @GetMapping("/types/display-names")
    public ResponseEntity<?> getAllTaskTypeDisplayNames() {
        return ResponseEntity.ok(TaskTypeUtils.getAllTaskTypeDisplayNames());
    }

    @GetMapping("/types/repetitive")
    public ResponseEntity<?> getRepetitiveTaskTypes() {
        return ResponseEntity.ok(TaskTypeUtils.getRepetitiveTaskTypes());
    }

    @GetMapping("/types/non-repetitive")
    public ResponseEntity<?> getNonRepetitiveTaskTypes() {
        return ResponseEntity.ok(TaskTypeUtils.getNonRepetitiveTaskTypes());
    }

    @GetMapping("/types/validate/{taskType}")
    public ResponseEntity<?> validateTaskType(@PathVariable String taskType) {
        boolean isValid = TaskTypeUtils.isValidTaskType(taskType);
        return ResponseEntity.ok(new CustomMessage("Task type validation result", isValid));
    }

    // Time Interval endpoints
    @GetMapping("/time-interval-units")
    public ResponseEntity<?> getAllTimeIntervalUnits() {
        return ResponseEntity.ok(TimeIntervalUnit.values());
    }

    @GetMapping("/time-interval-units/display-names")
    public ResponseEntity<?> getAllTimeIntervalUnitDisplayNames() {
        return ResponseEntity.ok(Arrays.stream(TimeIntervalUnit.values())
                .map(TimeIntervalUnit::getDisplayName)
                .collect(Collectors.toList()));
    }

    @GetMapping("/time-interval-units/frequency-names")
    public ResponseEntity<?> getAllTimeIntervalUnitFrequencyNames() {
        return ResponseEntity.ok(Arrays.stream(TimeIntervalUnit.values())
                .map(TimeIntervalUnit::getFrequencyName)
                .collect(Collectors.toList()));
    }

    // Repetitive task reminder endpoints
    @PostMapping("/repetitive/remind/{taskId}")
    public ResponseEntity<CustomMessage> sendRepetitiveTaskReminder(@PathVariable Long taskId) {
        TaskDto taskDto = taskService.getTaskById(taskId);
        if (!"REPETITIVE".equals(taskDto.getType())) {
            return ResponseEntity.badRequest()
                .body(new CustomMessage("Task is not repetitive"));
        }
        
        // Find the actual task entity
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        
        repetitiveTaskReminderService.sendRepetitiveTaskReminder(task);
        return ResponseEntity.ok(new CustomMessage("Repetitive task reminder sent successfully"));
    }

    @PostMapping("/repetitive/remind-all")
    public ResponseEntity<CustomMessage> sendAllRepetitiveTaskReminders() {
        List<Task> repetitiveTasks = taskRepository.findRepetitiveTasksNeedingReminders();
        for (Task task : repetitiveTasks) {
            repetitiveTaskReminderService.sendRepetitiveTaskReminder(task);
        }
        return ResponseEntity.ok(new CustomMessage("Sent reminders for " + repetitiveTasks.size() + " repetitive tasks"));
    }

    @GetMapping("/repetitive/needing-reminders")
    public ResponseEntity<?> getRepetitiveTasksNeedingReminders() {
        List<Task> tasks = taskRepository.findRepetitiveTasksNeedingReminders();
        List<RepetitiveTaskReminderDto> reminderDtos = tasks.stream()
            .map(RepetitiveTaskReminderDto::fromTask)
            .collect(Collectors.toList());
        return ResponseEntity.ok(reminderDtos);
    }

    @GetMapping("/repetitive/upcoming-due")
    public ResponseEntity<?> getRepetitiveTasksWithUpcomingDueDate(@RequestParam(defaultValue = "3") int daysAhead) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, daysAhead);
        Date dueDate = calendar.getTime();
        
        List<Task> tasks = taskRepository.findRepetitiveTasksWithUpcomingDueDate(dueDate);
        List<RepetitiveTaskReminderDto> reminderDtos = tasks.stream()
            .map(RepetitiveTaskReminderDto::fromTask)
            .collect(Collectors.toList());
        return ResponseEntity.ok(reminderDtos);
    }

    @PostMapping("/repetitive/process-completed")
    public ResponseEntity<CustomMessage> processCompletedRepetitiveTasks() {
        Date currentDate = new Date();
        List<Task> completedTasks = taskRepository.findCompletedRepetitiveTasksReadyForNextCycle(currentDate);
        
        for (Task task : completedTasks) {
            repetitiveTaskReminderService.createNextCycleForCompletedTask(task);
        }
        
        return ResponseEntity.ok(new CustomMessage("Processed " + completedTasks.size() + " completed repetitive tasks"));
    }
}