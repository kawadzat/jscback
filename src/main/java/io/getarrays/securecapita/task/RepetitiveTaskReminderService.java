package io.getarrays.securecapita.task;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Service for handling repetitive task reminders and notifications
 * 
 * @author SecureCapita
 * @version 1.0
 * @since 2024
 */
@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class RepetitiveTaskReminderService {

    private final TaskRepository taskRepository;
    private final EmailService emailService;

    /**
     * Send daily reminders for repetitive tasks that need status updates
     * Runs every day at 9:00 AM
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendDailyRepetitiveTaskReminders() {
        log.info("Starting daily repetitive task reminder process");
        
        List<Task> repetitiveTasks = taskRepository.findRepetitiveTasksNeedingReminders();
        
        for (Task task : repetitiveTasks) {
            sendRepetitiveTaskReminder(task);
        }
        
        log.info("Completed daily repetitive task reminder process. Sent {} reminders", repetitiveTasks.size());
    }

    /**
     * Send reminders for repetitive tasks that are due soon (within 3 days)
     * Runs every day at 10:00 AM
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendUpcomingDueDateReminders() {
        log.info("Starting upcoming due date reminder process");
        
        // Calculate date 3 days from now
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        Date reminderDate = calendar.getTime();
        
        List<Task> upcomingTasks = taskRepository.findRepetitiveTasksWithUpcomingDueDate(reminderDate);
        
        for (Task task : upcomingTasks) {
            sendUpcomingDueDateReminder(task);
        }
        
        log.info("Completed upcoming due date reminder process. Sent {} reminders", upcomingTasks.size());
    }

    /**
     * Process completed repetitive tasks and create next cycle
     * Runs every day at 11:00 AM
     */
    @Scheduled(cron = "0 0 11 * * ?")
    public void processCompletedRepetitiveTasks() {
        log.info("Starting completed repetitive task processing");
        
        Date currentDate = new Date();
        List<Task> completedTasks = taskRepository.findCompletedRepetitiveTasksReadyForNextCycle(currentDate);
        
        for (Task task : completedTasks) {
            createNextCycleForCompletedTask(task);
        }
        
        log.info("Completed repetitive task processing. Created {} new cycles", completedTasks.size());
    }

    /**
     * Send reminder for a specific repetitive task
     * 
     * @param task the task to send reminder for
     */
    public void sendRepetitiveTaskReminder(Task task) {
        if (task.getType() != TaskTypeEnum.REPETITIVE) {
            log.warn("Attempted to send repetitive task reminder for non-repetitive task: {}", task.getId());
            return;
        }

        for (User assignedUser : task.getAssignedUsers()) {
            String email = assignedUser.getEmail();
            String subject = "Reminder: Repetitive Task Status Update Required";
            
            String content = buildRepetitiveTaskReminderContent(task, assignedUser);
            
            try {
                emailService.sendEmail(email, subject, content);
                log.info("Sent repetitive task reminder to {} for task: {}", email, task.getTitle());
            } catch (Exception e) {
                log.error("Failed to send repetitive task reminder to {} for task {}: {}", 
                    email, task.getTitle(), e.getMessage());
            }
        }
    }

    /**
     * Send upcoming due date reminder for a repetitive task
     * 
     * @param task the task to send reminder for
     */
    public void sendUpcomingDueDateReminder(Task task) {
        if (task.getType() != TaskTypeEnum.REPETITIVE) {
            return;
        }

        for (User assignedUser : task.getAssignedUsers()) {
            String email = assignedUser.getEmail();
            String subject = "Upcoming Due Date: Repetitive Task";
            
            String content = buildUpcomingDueDateReminderContent(task, assignedUser);
            
            try {
                emailService.sendEmail(email, subject, content);
                log.info("Sent upcoming due date reminder to {} for task: {}", email, task.getTitle());
            } catch (Exception e) {
                log.error("Failed to send upcoming due date reminder to {} for task {}: {}", 
                    email, task.getTitle(), e.getMessage());
            }
        }
    }

    /**
     * Create next cycle for a completed repetitive task
     * 
     * @param completedTask the completed task to create next cycle for
     */
    public void createNextCycleForCompletedTask(Task completedTask) {
        if (completedTask.getType() != TaskTypeEnum.REPETITIVE || 
            completedTask.getStatus() != TaskStatusEnum.COMPLETED) {
            return;
        }

        // Create new task for next cycle
        Task nextCycleTask = new Task();
        nextCycleTask.setCode(generateNextCycleCode(completedTask.getCode()));
        nextCycleTask.setTitle(completedTask.getTitle() + " (Next Cycle)");
        nextCycleTask.setDescription(completedTask.getDescription());
        nextCycleTask.setType(TaskTypeEnum.REPETITIVE);
        nextCycleTask.setStatus(TaskStatusEnum.PENDING);
        nextCycleTask.setPriority(completedTask.getPriority());
        nextCycleTask.setInitiatedUser(completedTask.getInitiatedUser());
        nextCycleTask.setAssignedUsers(completedTask.getAssignedUsers());
        nextCycleTask.setTimeIntervalValue(completedTask.getTimeIntervalValue());
        nextCycleTask.setTimeIntervalUnit(completedTask.getTimeIntervalUnit());
        
        // Set dates for next cycle
        Date nextStartDate = completedTask.getNextRepetitionDate();
        nextCycleTask.setStartDate(nextStartDate);
        nextCycleTask.setInitiatedDate(new Date());
        
        // Calculate next due date and repetition date
        Date nextDueDate = calculateNextRepetitionDate(nextStartDate, 
            completedTask.getTimeIntervalValue(), 
            completedTask.getTimeIntervalUnit());
        nextCycleTask.setDueDate(nextDueDate);
        nextCycleTask.setNextRepetitionDate(calculateNextRepetitionDate(nextDueDate, 
            completedTask.getTimeIntervalValue(), 
            completedTask.getTimeIntervalUnit()));

        // Save the new task
        taskRepository.save(nextCycleTask);
        
        // Send notification about new cycle
        sendNewCycleNotification(nextCycleTask);
        
        log.info("Created next cycle for repetitive task: {} -> {}", 
            completedTask.getCode(), nextCycleTask.getCode());
    }

    /**
     * Build email content for repetitive task reminder
     */
    private String buildRepetitiveTaskReminderContent(Task task, User user) {
        return String.format("""
            <html>
            <body>
                <h2>Repetitive Task Reminder</h2>
                <p>Dear %s,</p>
                <p>This is a reminder to update the status of your repetitive task:</p>
                <ul>
                    <li><strong>Task:</strong> %s</li>
                    <li><strong>Code:</strong> %s</li>
                    <li><strong>Current Status:</strong> %s</li>
                    <li><strong>Due Date:</strong> %s</li>
                    <li><strong>Repetition:</strong> Every %d %s</li>
                </ul>
                <p>Please log into the system and update the task status to keep track of your progress.</p>
                <p>Best regards,<br>Task Management System</p>
            </body>
            </html>
            """,
            user.getFirstName(),
            task.getTitle(),
            task.getCode(),
            task.getStatus().name(),
            task.getDueDate(),
            task.getTimeIntervalValue(),
            task.getTimeIntervalUnit().getDisplayName()
        );
    }

    /**
     * Build email content for upcoming due date reminder
     */
    private String buildUpcomingDueDateReminderContent(Task task, User user) {
        return String.format("""
            <html>
            <body>
                <h2>Upcoming Due Date Reminder</h2>
                <p>Dear %s,</p>
                <p>Your repetitive task is due soon:</p>
                <ul>
                    <li><strong>Task:</strong> %s</li>
                    <li><strong>Code:</strong> %s</li>
                    <li><strong>Due Date:</strong> %s</li>
                    <li><strong>Current Status:</strong> %s</li>
                </ul>
                <p>Please complete this task before the due date to maintain your task schedule.</p>
                <p>Best regards,<br>Task Management System</p>
            </body>
            </html>
            """,
            user.getFirstName(),
            task.getTitle(),
            task.getCode(),
            task.getDueDate(),
            task.getStatus().name()
        );
    }

    /**
     * Send notification about new cycle creation
     */
    private void sendNewCycleNotification(Task newTask) {
        for (User assignedUser : newTask.getAssignedUsers()) {
            String email = assignedUser.getEmail();
            String subject = "New Cycle Created: " + newTask.getTitle();
            
            String content = String.format("""
                <html>
                <body>
                    <h2>New Task Cycle Created</h2>
                    <p>Dear %s,</p>
                    <p>A new cycle has been created for your repetitive task:</p>
                    <ul>
                        <li><strong>Task:</strong> %s</li>
                        <li><strong>Code:</strong> %s</li>
                        <li><strong>Start Date:</strong> %s</li>
                        <li><strong>Due Date:</strong> %s</li>
                    </ul>
                    <p>Please log into the system to view and manage this new task cycle.</p>
                    <p>Best regards,<br>Task Management System</p>
                </body>
                </html>
                """,
                assignedUser.getFirstName(),
                newTask.getTitle(),
                newTask.getCode(),
                newTask.getStartDate(),
                newTask.getDueDate()
            );
            
            try {
                emailService.sendEmail(email, subject, content);
                log.info("Sent new cycle notification to {} for task: {}", email, newTask.getTitle());
            } catch (Exception e) {
                log.error("Failed to send new cycle notification to {} for task {}: {}", 
                    email, newTask.getTitle(), e.getMessage());
            }
        }
    }

    /**
     * Generate code for next cycle
     */
    private String generateNextCycleCode(String originalCode) {
        return originalCode + "-NC-" + System.currentTimeMillis();
    }

    /**
     * Calculate next repetition date
     */
    private Date calculateNextRepetitionDate(Date currentDate, Integer intervalValue, TimeIntervalUnit intervalUnit) {
        if (currentDate == null || intervalValue == null || intervalUnit == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        switch (intervalUnit) {
            case DAY:
                calendar.add(Calendar.DAY_OF_MONTH, intervalValue);
                break;
            case WEEK:
                calendar.add(Calendar.WEEK_OF_YEAR, intervalValue);
                break;
            case MONTH:
                calendar.add(Calendar.MONTH, intervalValue);
                break;
            case QUARTER:
                calendar.add(Calendar.MONTH, intervalValue * 3);
                break;
            case YEAR:
                calendar.add(Calendar.YEAR, intervalValue);
                break;
            default:
                return null;
        }

        return calendar.getTime();
    }
} 