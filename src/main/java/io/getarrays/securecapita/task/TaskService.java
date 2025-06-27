package io.getarrays.securecapita.task;

import io.getarrays.securecapita.codegenerator.CodeGeneratorService;
import io.getarrays.securecapita.domain.PageResponseDto;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.dtomapper.UserDTOMapper;
import io.getarrays.securecapita.exception.BadRequestException;
import io.getarrays.securecapita.exception.NotAuthorizedException;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.repository.UserRepository;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {


    private final TaskRepository taskRepository;

    private final UserRepository<User> userRepository;

    private final UserRepository1 userRepository1;

    private final CodeGeneratorService codeGeneratorService;

    private final EmailService emailService;

    public TaskDto getTaskById(Long taskId) {
        return entityToDto(findTaskByIdOrThrow(taskId));
    }

    private Task findTaskByIdOrThrow(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found " +
                "with id " + taskId));
    }

    public PageResponseDto<TaskDto> getAllTasks(UserDTO currentUser, TaskSearchDto searchDto) {
        Page<Task> page = taskRepository.findTasksByFilters(currentUser.getId(), searchDto.getOwnedByMe(),
                searchDto.getAssignedToMe(), searchDto.getStationId(), searchDto.getDepartmentId(),
                PageRequest.of(searchDto.getPage(), searchDto.getSize(), Sort.by("lastModifiedDate").descending()));
        return new PageResponseDto<>(page.getContent().stream().map(this::entityToDto).toList(), page);
    }

    public void deleteTask(Long taskId) {
        getTaskById(taskId);
        taskRepository.deleteById(taskId);
    }

    public TaskDto createTask(UserDTO currentUser, TaskDto taskDto) {
        Task task = dtoToEntity(currentUser, null, taskDto);
        task = taskRepository.save(task);
        sendReminderEmail(task);
        return entityToDto(task);
    }

    public TaskDto updateTask(UserDTO currentUser, Long taskId, TaskDto taskDto) {
        Task task = dtoToEntity(currentUser, taskId, taskDto);
        task = taskRepository.save(task);
        sendReminderEmail(task);
        return entityToDto(task);
    }

    private Task dtoToEntity(UserDTO currentUser, Long taskId, TaskDto taskDto) {
        Task task;
        if (taskId == null) {
            task = new Task();
            task.setStatus(TaskStatusEnum.PENDING);
            task.setCode(generateCode());
            task.setInitiatedDate(new Date());
        } else {
            task = findTaskByIdOrThrow(taskId);
            if (Arrays.asList(TaskStatusEnum.CANCELLED, TaskStatusEnum.IN_PROGRESS, TaskStatusEnum.COMPLETED).contains(task.getStatus())) {
                throw new BadRequestException("Task cannot be updated only in pending state.");
            }
        }
        task.setStartDate(taskDto.getStartDate());
        task.setDueDate(taskDto.getDueDate());
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setPriority(PriorityEnum.valueOf(taskDto.getPriority()));
        task.setStartDate(taskDto.getStartDate());
        task.setDueDate(taskDto.getDueDate());
        task.setStatus(TaskStatusEnum.valueOf(taskDto.getStatus()));
        task.setInitiatedUser(userRepository.get(currentUser.getId()));
        Set<User> assignedUsers = new HashSet<>(userRepository1.findAllById(taskDto.getAssignedUserIds()));
        if (assignedUsers.size() != taskDto.getAssignedUserIds().size()) {
            throw new ResourceNotFoundException("One or more assignedUserIds don't exist.");
        }
        task.setAssignedUsers(assignedUsers);
        return task;
    }

    private TaskDto entityToDto(Task taskEntity) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(taskEntity.getId());
        taskDto.setCode(taskEntity.getCode());
        taskDto.setTitle(taskEntity.getTitle());
        taskDto.setDescription(taskEntity.getDescription());
        taskDto.setStartDate(taskEntity.getStartDate());
        taskDto.setDueDate(taskEntity.getDueDate());
        taskDto.setPriority(taskEntity.getPriority().name());
        taskDto.setStatus(taskEntity.getStatus().name());
        taskDto.setInitiatedUser(UserDTOMapper.fromUser(taskEntity.getInitiatedUser()));
        taskDto.setAssignedUsers(taskEntity.getAssignedUsers().stream().map(UserDTOMapper::fromUser).collect(Collectors.toSet()));
        taskDto.setAssignedUserIds(taskEntity.getAssignedUsers().stream().map(User::getId).collect(Collectors.toSet()));
        return taskDto;
    }

    private String generateCode() {
        int code = codeGeneratorService.generateCode("Task-");
        return "Task-" + code;
    }

    public void updateTaskStatus(UserDTO currentUser, Long taskId, String status) {
        Task task = findTaskByIdOrThrow(taskId);

        // Check if the current user is either the assigned user or the initiating user.
        if (!(task.getAssignedUsers().stream().map(User::getId).toList().contains(currentUser.getId()) || task.getInitiatedUser().getId().equals(currentUser.getId()))) {
            throw new NotAuthorizedException("You are not authorized to update this task status");
        }

        // Prevent updates if the current status is CANCELLED or COMPLETED.
        TaskStatusEnum currentStatus = task.getStatus();
        if (currentStatus == TaskStatusEnum.CANCELLED || currentStatus == TaskStatusEnum.COMPLETED) {
            throw new BadRequestException("Task status is already " + currentStatus.name().toLowerCase() + " and " +
                    "cannot be changed.");
        }
        try {
            TaskStatusEnum newStatus = TaskStatusEnum.valueOf(status);
            task.setLastModifiedDate(new Date());
            task.setLastModifiedBy(currentUser.getEmail());
            task.setStatus(newStatus);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid status value");
        }

        task = taskRepository.save(task);
        sendUpdateEmail(task);
    }

    private void sendUpdateEmail(Task task) {
        task.getAssignedUsers().stream().forEach(e -> {
            String email = e.getEmail();
            String subject = "Task Update";
            String content =
                    "Dear " + e.getFirstName() + ",\n\n" + "Your task \"" + task.getTitle() + "\" " + "is " + "now " + task.getStatus().name().toLowerCase() + ".";
            emailService.sendEmail(email, subject, content);
        });
    }

    private void sendReminderEmail(Task task) {
        task.getAssignedUsers().stream().forEach(e -> {
            String email = e.getEmail();
            String subject = "Reminder: Pending Task";
            String content =
                    "Dear " + e.getFirstName() + ",\n\n" + "Your task \"" + task.getTitle() + "\" " + "is" + " still "
                            + "pending. " + "Please take the necessary actions to update its status.";
            emailService.sendEmail(email, subject, content);
        });
    }

    // This cron expression schedules the task to run every day at 8 AM.
    @Scheduled(cron = "0 0 8 * * ?")
    private void sendPendingTaskEmails() {
        Pageable pageable = PageRequest.of(0, 50); // Adjust page size as needed
        // Fetch tasks that are in the PENDING status
        Page<Task> pendingTasks = taskRepository.findByStatusIn(Arrays.asList(TaskStatusEnum.PENDING,
                TaskStatusEnum.IN_PROGRESS), pageable);
        pendingTasks.forEach(this::sendReminderEmail);
    }




    public int getPendingTaskCountForUser(Long userId) {
        return taskRepository.countPendingTasksForUser(userId);
    }


    public int getCompletedTaskCountForUser(Long userId) {
        return taskRepository.countCompletedTasksForUser(userId);
    }

    public TaskReportDto getStatusReport(Date dateFrom, Date dateTo, Long userId, Long stationId, Long departmentId) {
        if (dateFrom == null) {
            dateFrom = new Date(0); // epoch
        }
        if (dateTo == null) {
            dateTo = new Date(); // now
        }

        List<Object[]> results = taskRepository.countTasksByStatusWithFilters(dateFrom, dateTo, userId, stationId, departmentId);

        TaskReportDto report = new TaskReportDto();
        report.setPendingCount(0L);
        report.setInProgressCount(0L);
        report.setCompletedCount(0L);

        for (Object[] result : results) {
            TaskStatusEnum status = (TaskStatusEnum) result[0];
            Long count = (Long) result[1];
            switch (status) {
                case PENDING -> report.setPendingCount(count);
                case IN_PROGRESS -> report.setInProgressCount(count);
                case COMPLETED -> report.setCompletedCount(count);
            }
        }
        return report;
    }

    public List<UserWiseTaskReportDto> getUserWiseStatusReport(Date dateFrom, Date dateTo, Long userId, Long stationId, Long departmentId) {
        if (dateFrom == null) {
            dateFrom = new Date(0);
        }
        if (dateTo == null) {
            dateTo = new Date();
        }

        List<Object[]> results = taskRepository.countTasksByUserAndStatusWithFilters(dateFrom, dateTo, userId, stationId, departmentId);

        // Use a Map to accumulate task stats by user
        Map<Long, UserWiseTaskReportDto> reportMap = new HashMap<>();

        for (Object[] row : results) {
            User user = (User) row[0];
            TaskStatusEnum status = (TaskStatusEnum) row[1];
            Long count = (Long) row[2];

            UserWiseTaskReportDto userReport = reportMap.computeIfAbsent(user.getId(), id -> {
                UserWiseTaskReportDto dto = new UserWiseTaskReportDto();
                dto.setUser(UserDTOMapper.fromUser(user));
                dto.setTaskStats(new TaskReportDto());
                dto.getTaskStats().setPendingCount(0L);
                dto.getTaskStats().setInProgressCount(0L);
                dto.getTaskStats().setCompletedCount(0L);
                return dto;
            });

            switch (status) {
                case PENDING -> userReport.getTaskStats().setPendingCount(count);
                case IN_PROGRESS -> userReport.getTaskStats().setInProgressCount(count);
                case COMPLETED -> userReport.getTaskStats().setCompletedCount(count);
            }
        }

        return new ArrayList<>(reportMap.values());
    }

}