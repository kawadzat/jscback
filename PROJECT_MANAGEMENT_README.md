# Project Management System

A comprehensive project management system built with Spring Boot, providing full CRUD operations for projects, tasks, and milestones with advanced features like progress tracking, team management, and reporting.

## Features

### Core Project Management
- **Project CRUD Operations**: Create, read, update, and delete projects
- **Project Status Management**: Track project lifecycle (Planning, Active, On Hold, Completed, etc.)
- **Priority Management**: Set and manage project priorities (Low, Medium, High, Critical, Urgent)
- **Project Types**: Categorize projects (Development, Maintenance, Research, Infrastructure, etc.)
- **Budget Tracking**: Monitor project budgets and actual costs
- **Progress Tracking**: Track project completion percentage
- **Team Management**: Assign project managers, clients, and team members

### Advanced Features
- **Risk Management**: Assess and track project risks
- **Quality Metrics**: Track quality scores and customer satisfaction
- **Template Projects**: Create reusable project templates
- **Parent-Child Projects**: Support for project hierarchies
- **Search and Filtering**: Advanced search capabilities
- **Dashboard Statistics**: Project overview and analytics

### Task Management
- **Task CRUD Operations**: Full task lifecycle management
- **Task Assignment**: Assign tasks to team members
- **Task Dependencies**: Define task dependencies
- **Time Tracking**: Track estimated vs actual hours
- **Milestone Tracking**: Mark important project milestones
- **Acceptance Criteria**: Define task completion criteria

### Reporting and Analytics
- **Overdue Projects**: Identify delayed projects
- **High-Risk Projects**: Flag projects requiring attention
- **Low Progress Projects**: Identify stalled projects
- **Budget Utilization**: Track budget vs actual spending
- **Team Performance**: Monitor team member contributions

## API Endpoints

### Project Management

#### Create Project
```http
POST /api/v1/projects
Content-Type: application/json

{
  "name": "Website Redesign",
  "description": "Complete redesign of company website",
  "startDate": "2024-01-15",
  "endDate": "2024-03-15",
  "status": "PLANNING",
  "priority": "HIGH",
  "type": "DEVELOPMENT",
  "budget": 50000.00,
  "projectManagerId": 1,
  "clientId": 2,
  "teamMemberIds": [3, 4, 5]
}
```

#### Get All Projects
```http
GET /api/v1/projects?page=0&size=10
```

#### Get Project by ID
```http
GET /api/v1/projects/{projectId}
```

#### Update Project
```http
PUT /api/v1/projects/{projectId}
Content-Type: application/json

{
  "name": "Updated Project Name",
  "progressPercentage": 75
}
```

#### Delete Project
```http
DELETE /api/v1/projects/{projectId}
```

#### Filter Projects by Status
```http
GET /api/v1/projects/status/ACTIVE?page=0&size=10
```

#### Filter Projects by Priority
```http
GET /api/v1/projects/priority/HIGH?page=0&size=10
```

#### Filter Projects by Type
```http
GET /api/v1/projects/type/DEVELOPMENT?page=0&size=10
```

#### Get Projects by Manager
```http
GET /api/v1/projects/manager/{managerId}?page=0&size=10
```

#### Get Projects by Team Member
```http
GET /api/v1/projects/team-member/{memberId}?page=0&size=10
```

#### Search Projects
```http
GET /api/v1/projects/search?searchTerm=website&page=0&size=10
```

#### Update Project Progress
```http
PUT /api/v1/projects/{projectId}/progress?progressPercentage=85
```

#### Update Project Status
```http
PUT /api/v1/projects/{projectId}/status?status=ACTIVE
```

#### Add Team Member
```http
POST /api/v1/projects/{projectId}/team-members/{userId}
```

#### Remove Team Member
```http
DELETE /api/v1/projects/{projectId}/team-members/{userId}
```

#### Update Project Budget
```http
PUT /api/v1/projects/{projectId}/budget?budget=75000.00
```

#### Update Actual Cost
```http
PUT /api/v1/projects/{projectId}/actual-cost?actualCost=45000.00
```

### Reporting Endpoints

#### Get Overdue Projects
```http
GET /api/v1/projects/overdue
```

#### Get High-Risk Projects
```http
GET /api/v1/projects/high-risk
```

#### Get Low Progress Projects
```http
GET /api/v1/projects/low-progress
```

#### Get Dashboard Statistics
```http
GET /api/v1/projects/dashboard/stats
```

#### Get Recent Projects
```http
GET /api/v1/projects/dashboard/recent?limit=5
```

## Data Models

### Project Entity
```java
@Entity
@Table(name = "projects")
public class Project extends Auditable<String> {
    private Long id;
    private String name;
    private String description;
    private String code;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private ProjectStatus status;
    private ProjectPriority priority;
    private ProjectType type;
    private BigDecimal budget;
    private BigDecimal actualCost;
    private Integer progressPercentage;
    private User projectManager;
    private User client;
    private Set<User> teamMembers;
    private String location;
    private String department;
    private String tags;
    private RiskLevel riskLevel;
    private String riskDescription;
    private Integer qualityScore;
    private Integer customerSatisfactionScore;
    private Boolean isActive;
    private Boolean isTemplate;
    private Long parentProjectId;
    private String notes;
    private String completionCriteria;
    private String successMetrics;
}
```

### ProjectTask Entity
```java
@Entity
@Table(name = "project_tasks")
public class ProjectTask extends Auditable<String> {
    private Long id;
    private String name;
    private String description;
    private Project project;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate startDate;
    private LocalDate dueDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private Integer estimatedHours;
    private Integer actualHours;
    private Integer progressPercentage;
    private User assignedTo;
    private User createdBy;
    private Set<User> assignees;
    private String tags;
    private String notes;
    private Boolean isMilestone;
    private Boolean isBlocked;
    private String blockReason;
    private String dependencies;
    private String acceptanceCriteria;
    private String definitionOfDone;
    private Integer storyPoints;
    private BigDecimal cost;
}
```

### ProjectMilestone Entity
```java
@Entity
@Table(name = "project_milestones")
public class ProjectMilestone extends Auditable<String> {
    private Long id;
    private String name;
    private String description;
    private Project project;
    private LocalDate dueDate;
    private LocalDate actualDate;
    private MilestoneStatus status;
    private Integer progressPercentage;
    private User responsiblePerson;
    private Set<User> assignees;
    private String notes;
    private Boolean isCritical;
    private String dependencies;
    private String successCriteria;
}
```

## Enums

### ProjectStatus
- `PLANNING` - Project is in planning phase
- `ACTIVE` - Project is currently active
- `ON_HOLD` - Project is temporarily on hold
- `COMPLETED` - Project has been completed
- `CANCELLED` - Project has been cancelled
- `SUSPENDED` - Project is suspended
- `REVIEW` - Project is under review
- `APPROVED` - Project has been approved
- `REJECTED` - Project has been rejected
- `ARCHIVED` - Project has been archived

### ProjectPriority
- `LOW` - Low priority
- `MEDIUM` - Medium priority
- `HIGH` - High priority
- `CRITICAL` - Critical priority
- `URGENT` - Urgent priority

### ProjectType
- `DEVELOPMENT` - Software development
- `MAINTENANCE` - System maintenance
- `RESEARCH` - Research projects
- `INFRASTRUCTURE` - Infrastructure projects
- `MIGRATION` - Data/system migration
- `UPGRADE` - System upgrades
- `CONSULTING` - Consulting projects
- `TRAINING` - Training projects
- `DOCUMENTATION` - Documentation projects
- `TESTING` - Testing projects
- `DEPLOYMENT` - Deployment projects
- `INTEGRATION` - Integration projects
- `CUSTOMIZATION` - Customization projects
- `SUPPORT` - Support projects
- `OTHER` - Other project types

### RiskLevel
- `LOW` - Low risk
- `MEDIUM` - Medium risk
- `HIGH` - High risk
- `CRITICAL` - Critical risk

### TaskStatus
- `TODO` - Task is to do
- `IN_PROGRESS` - Task is in progress
- `IN_REVIEW` - Task is under review
- `TESTING` - Task is being tested
- `DONE` - Task is completed
- `BLOCKED` - Task is blocked
- `CANCELLED` - Task is cancelled
- `ON_HOLD` - Task is on hold

### TaskPriority
- `LOW` - Low priority
- `MEDIUM` - Medium priority
- `HIGH` - High priority
- `CRITICAL` - Critical priority
- `URGENT` - Urgent priority

### MilestoneStatus
- `PLANNED` - Milestone is planned
- `IN_PROGRESS` - Milestone is in progress
- `COMPLETED` - Milestone is completed
- `DELAYED` - Milestone is delayed
- `CANCELLED` - Milestone is cancelled
- `ON_HOLD` - Milestone is on hold

## Database Schema

The system creates the following database tables:

- `projects` - Main project table
- `project_tasks` - Project tasks table
- `project_milestones` - Project milestones table
- `project_team_members` - Many-to-many relationship between projects and team members
- `project_task_assignees` - Many-to-many relationship between tasks and assignees
- `project_milestone_assignees` - Many-to-many relationship between milestones and assignees

## Authentication and Authorization

The system uses Spring Security for authentication and authorization. All endpoints require authentication, and some operations may require specific roles or permissions.

## Error Handling

The system provides comprehensive error handling with appropriate HTTP status codes:

- `200 OK` - Successful operation
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Validation

The system includes comprehensive validation for:

- Required fields
- Field length limits
- Date validations (start date before end date)
- Numeric range validations (progress percentage 0-100)
- Budget and cost validations (non-negative values)

## Computed Fields

The system automatically calculates several computed fields:

- `daysRemaining` - Days remaining until project/task due date
- `isOverdue` - Whether project/task is overdue
- `isOnTrack` - Whether project/task is on track
- `budgetUtilization` - Budget utilization percentage
- `statusColor` - Color coding for status display
- `priorityColor` - Color coding for priority display

## Usage Examples

### Creating a New Project
```bash
curl -X POST http://localhost:8080/api/v1/projects \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "name": "E-commerce Platform Development",
    "description": "Develop a new e-commerce platform for online retail",
    "startDate": "2024-02-01",
    "endDate": "2024-06-30",
    "status": "PLANNING",
    "priority": "HIGH",
    "type": "DEVELOPMENT",
    "budget": 150000.00,
    "projectManagerId": 1,
    "clientId": 2,
    "teamMemberIds": [3, 4, 5, 6]
  }'
```

### Updating Project Progress
```bash
curl -X PUT "http://localhost:8080/api/v1/projects/1/progress?progressPercentage=75" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Getting Overdue Projects
```bash
curl -X GET http://localhost:8080/api/v1/projects/overdue \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## Future Enhancements

- **Time Tracking**: Detailed time tracking for tasks
- **Resource Management**: Resource allocation and capacity planning
- **Gantt Charts**: Visual project timeline representation
- **Kanban Boards**: Visual task management
- **File Attachments**: Support for project documents
- **Comments and Discussions**: Team collaboration features
- **Notifications**: Email and push notifications
- **Mobile App**: Mobile application for project management
- **Integration**: Integration with external tools (Jira, Slack, etc.)
- **Advanced Analytics**: Detailed project analytics and reporting
- **Workflow Automation**: Automated project workflows
- **Templates**: Pre-built project templates for common project types

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 