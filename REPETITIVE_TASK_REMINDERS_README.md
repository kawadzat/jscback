# Repetitive Task Reminder System

## Overview

The Repetitive Task Reminder System automatically sends email reminders to assigned users for repetitive tasks that require status updates. This system ensures that repetitive tasks are properly tracked and completed on schedule.

## Features

### 1. Automated Email Reminders
- **Daily Reminders**: Sent every day at 9:00 AM for all repetitive tasks in PENDING or IN_PROGRESS status
- **Upcoming Due Date Reminders**: Sent every day at 10:00 AM for tasks due within 3 days
- **New Cycle Notifications**: Sent when a new cycle is created for completed repetitive tasks

### 2. Automatic Cycle Management
- **Next Cycle Creation**: Automatically creates new task cycles when repetitive tasks are completed
- **Date Calculation**: Calculates next due dates and repetition dates based on time intervals
- **Status Tracking**: Maintains proper status flow for repetitive tasks

### 3. Manual Reminder Controls
- **Individual Reminders**: Send reminders for specific repetitive tasks
- **Bulk Reminders**: Send reminders for all repetitive tasks needing updates
- **Manual Processing**: Process completed repetitive tasks manually

## System Components

### 1. RepetitiveTaskReminderService
The core service that handles all reminder logic and scheduling.

**Key Methods:**
- `sendDailyRepetitiveTaskReminders()` - Daily scheduled reminders
- `sendUpcomingDueDateReminders()` - Due date warnings
- `processCompletedRepetitiveTasks()` - Cycle management
- `sendRepetitiveTaskReminder(Task task)` - Individual reminders
- `createNextCycleForCompletedTask(Task task)` - Next cycle creation

### 2. TaskRepository Extensions
New query methods for finding repetitive tasks:

```java
// Find all repetitive tasks needing reminders
List<Task> findRepetitiveTasksNeedingReminders();

// Find repetitive tasks due for reminders
List<Task> findRepetitiveTasksDueForReminders(Date reminderDate);

// Find repetitive tasks with upcoming due dates
List<Task> findRepetitiveTasksWithUpcomingDueDate(Date dueDate);

// Find completed repetitive tasks ready for next cycle
List<Task> findCompletedRepetitiveTasksReadyForNextCycle(Date currentDate);
```

### 3. Scheduling Configuration
The `SchedulingConfig` class enables Spring's scheduling functionality:

```java
@Configuration
@EnableScheduling
public class SchedulingConfig {
    // Enables @Scheduled annotations
}
```

## API Endpoints

### Repetitive Task Reminder Endpoints

#### 1. Send Individual Reminder
```http
POST /task/repetitive/remind/{taskId}
```
Sends a reminder for a specific repetitive task.

**Response:**
```json
{
    "message": "Repetitive task reminder sent successfully"
}
```

#### 2. Send All Reminders
```http
POST /task/repetitive/remind-all
```
Sends reminders for all repetitive tasks that need updates.

**Response:**
```json
{
    "message": "Sent reminders for 5 repetitive tasks"
}
```

#### 3. Get Tasks Needing Reminders
```http
GET /task/repetitive/needing-reminders
```
Returns all repetitive tasks that need status update reminders.

**Response:**
```json
[
    {
        "taskId": 1,
        "taskCode": "TASK-001",
        "taskTitle": "Weekly Report",
        "taskStatus": "PENDING",
        "dueDate": "2024-01-15T00:00:00.000Z",
        "nextRepetitionDate": "2024-01-22T00:00:00.000Z",
        "timeIntervalValue": 7,
        "timeIntervalUnit": "Days",
        "assignedUserEmails": ["user@example.com"],
        "assignedUserNames": ["John Doe"]
    }
]
```

#### 4. Get Upcoming Due Tasks
```http
GET /task/repetitive/upcoming-due?daysAhead=3
```
Returns repetitive tasks due within the specified number of days.

**Parameters:**
- `daysAhead` (optional): Number of days ahead to check (default: 3)

#### 5. Process Completed Tasks
```http
POST /task/repetitive/process-completed
```
Manually processes completed repetitive tasks and creates next cycles.

**Response:**
```json
{
    "message": "Processed 2 completed repetitive tasks"
}
```

## Scheduled Jobs

### 1. Daily Repetitive Task Reminders
- **Schedule**: Every day at 9:00 AM
- **Purpose**: Send reminders for all repetitive tasks in PENDING or IN_PROGRESS status
- **Cron Expression**: `0 0 9 * * ?`

### 2. Upcoming Due Date Reminders
- **Schedule**: Every day at 10:00 AM
- **Purpose**: Send warnings for tasks due within 3 days
- **Cron Expression**: `0 0 10 * * ?`

### 3. Completed Task Processing
- **Schedule**: Every day at 11:00 AM
- **Purpose**: Process completed repetitive tasks and create next cycles
- **Cron Expression**: `0 0 11 * * ?`

## Email Templates

### 1. Repetitive Task Reminder
```html
<h2>Repetitive Task Reminder</h2>
<p>Dear [User Name],</p>
<p>This is a reminder to update the status of your repetitive task:</p>
<ul>
    <li><strong>Task:</strong> [Task Title]</li>
    <li><strong>Code:</strong> [Task Code]</li>
    <li><strong>Current Status:</strong> [Status]</li>
    <li><strong>Due Date:</strong> [Due Date]</li>
    <li><strong>Repetition:</strong> Every [Interval] [Unit]</li>
</ul>
<p>Please log into the system and update the task status to keep track of your progress.</p>
```

### 2. Upcoming Due Date Reminder
```html
<h2>Upcoming Due Date Reminder</h2>
<p>Dear [User Name],</p>
<p>Your repetitive task is due soon:</p>
<ul>
    <li><strong>Task:</strong> [Task Title]</li>
    <li><strong>Code:</strong> [Task Code]</li>
    <li><strong>Due Date:</strong> [Due Date]</li>
    <li><strong>Current Status:</strong> [Status]</li>
</ul>
<p>Please complete this task before the due date to maintain your task schedule.</p>
```

### 3. New Cycle Notification
```html
<h2>New Task Cycle Created</h2>
<p>Dear [User Name],</p>
<p>A new cycle has been created for your repetitive task:</p>
<ul>
    <li><strong>Task:</strong> [Task Title]</li>
    <li><strong>Code:</strong> [Task Code]</li>
    <li><strong>Start Date:</strong> [Start Date]</li>
    <li><strong>Due Date:</strong> [Due Date]</li>
</ul>
<p>Please log into the system to view and manage this new task cycle.</p>
```

## Configuration

### Email Configuration
Ensure your email service is properly configured in `application.yml`:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

### Scheduling Configuration
The scheduling is enabled by the `SchedulingConfig` class. You can customize the cron expressions in the `RepetitiveTaskReminderService`:

```java
// Daily reminders at 9:00 AM
@Scheduled(cron = "0 0 9 * * ?")

// Upcoming due reminders at 10:00 AM
@Scheduled(cron = "0 0 10 * * ?")

// Process completed tasks at 11:00 AM
@Scheduled(cron = "0 0 11 * * ?")
```

## Usage Examples

### 1. Creating a Repetitive Task
```json
{
    "title": "Weekly System Backup",
    "description": "Perform weekly backup of all systems",
    "type": "REPETITIVE",
    "priority": "HIGH",
    "timeIntervalValue": 7,
    "timeIntervalUnit": "DAY",
    "assignedUserIds": [1, 2, 3]
}
```

### 2. Manual Reminder Trigger
```bash
# Send reminder for specific task
curl -X POST http://localhost:8080/task/repetitive/remind/123

# Send reminders for all repetitive tasks
curl -X POST http://localhost:8080/task/repetitive/remind-all
```

### 3. Check Tasks Needing Reminders
```bash
# Get all repetitive tasks needing reminders
curl -X GET http://localhost:8080/task/repetitive/needing-reminders

# Get tasks due within 5 days
curl -X GET "http://localhost:8080/task/repetitive/upcoming-due?daysAhead=5"
```

## Database Schema Changes

The system uses the existing Task entity with the following fields for repetitive tasks:

- `type`: Set to "REPETITIVE" for repetitive tasks
- `timeIntervalValue`: Number of time units between repetitions
- `timeIntervalUnit`: Time unit (DAY, WEEK, MONTH, QUARTER, YEAR)
- `nextRepetitionDate`: Date of the next repetition cycle

## Monitoring and Logging

The system provides comprehensive logging for monitoring:

```java
// Log when reminders are sent
log.info("Sent repetitive task reminder to {} for task: {}", email, task.getTitle());

// Log when new cycles are created
log.info("Created next cycle for repetitive task: {} -> {}", 
    completedTask.getCode(), nextCycleTask.getCode());

// Log errors
log.error("Failed to send repetitive task reminder to {} for task {}: {}", 
    email, task.getTitle(), e.getMessage());
```

## Error Handling

The system includes robust error handling:

1. **Email Failures**: Individual email failures don't stop the entire process
2. **Invalid Tasks**: Non-repetitive tasks are skipped with warnings
3. **Missing Data**: Tasks with missing time interval data are logged and skipped
4. **Database Errors**: Repository errors are caught and logged

## Best Practices

1. **Time Zone Considerations**: Ensure your server timezone is correctly configured
2. **Email Rate Limiting**: Consider implementing rate limiting for email sending
3. **Monitoring**: Set up alerts for failed reminder sends
4. **Testing**: Test the scheduling with shorter intervals during development
5. **Backup**: Ensure database backups include task data

## Troubleshooting

### Common Issues

1. **Reminders Not Sending**
   - Check email configuration
   - Verify scheduling is enabled
   - Check server timezone

2. **Wrong Due Dates**
   - Verify time interval calculations
   - Check date format consistency

3. **Duplicate Cycles**
   - Ensure proper transaction handling
   - Check for concurrent processing

### Debug Mode

Enable debug logging for the reminder service:

```yaml
logging:
  level:
    io.getarrays.securecapita.task.RepetitiveTaskReminderService: DEBUG
```

This will provide detailed information about reminder processing and email sending. 