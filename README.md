# Task Manager Microservice

## Overview
Task management microservice handling task and issue tracking. Part of a distributed system (under development) with separate user management and security services.

## Features
- Task Management with hierarchical structure (subtasks)
- Issue Tracking and linking to tasks
- Task Assignment and Due Date management
- Configurable statuses and priorities
- Activity Logging and Audit Trail
- Standardized Error Handling

## Tech Stack
- Spring Boot
- PostgreSQL
- Docker

## Architecture

### Domain Model
The system uses a layered architecture with clear separation of concerns:
- Base Classes
    - `BaseEntity`: Common fields for auditing (id, created_at, updated_at, etc.)
    - `BaseLookupEntity`: Common structure for lookup tables
- Domain Entities
    - Tasks and Issues (main entities)
    - Lookup tables for statuses and configurations
    - Closure table for hierarchical task relationships
- Activity Logging
    - AOP-based logging of all operations
    - Detailed audit trail

### Database Design
#### Core Tables
1. `task`
    - Inherits from BaseEntity
    - Primary operations table
    - Tracks task metadata and status

2. `issue`
    - Inherits from BaseEntity
    - Linked to tasks
    - Tracks problems and concerns

3. `task_closure`
    - Implements hierarchical relationship between tasks
    - Enables efficient tree traversal
    - Tracks ancestor-descendant relationships with depth

#### Lookup Tables
- `task_status_lk`
- `task_priority_lk`
- `issue_status_lk`
- `issue_criticality_lk`

#### Audit Tables
- `activity_log`
    - Tracks all system operations
    - Maintains audit trail

### Error Handling
Centralized error handling using `@ControllerAdvice` with standardized error responses:
```json
{
    "timestamp": "2024-02-05T10:00:00.000Z",
    "internalCode": 1000,
    "message": "Error message",
    "description": "Detailed error description"
}
```

Error Categories:
- Validation (1000-1999)
- Task Operations (2000-2099)
- Issue Operations (2100-2199)
- Lookup Operations (3000-3999)
- System Errors (5000-5999)

## Data Models

### BaseEntity (Abstract)
```java
- id: UUID
- createdAt: LocalDateTime
- createdBy: UUID
- updatedAt: LocalDateTime
- updatedBy: UUID
- isActive: Boolean
```

### Task
```java
Extends BaseEntity:
- name: String
- assigneeId: UUID
- dueDate: LocalDateTime
- description: String
- statusId: Integer
- priorityId: Integer
```

### TaskClosure
```java
- ancestorTaskId: UUID
- descendantTaskId: UUID
- depth: Integer
```

### Issue
```java
Extends BaseEntity:
- name: String
- description: String
- statusId: Integer
- criticalityId: Integer
- taskId: UUID
```

### Activity Log
```java
- id: UUID
- createdAt: Instant
- updatedAt: Instant
- isActive: Boolean
- httpMethod: String
- uri: String
- operation: String
- operationReferenceId: String
- message: String
- userId: UUID
```

### Lookup Entities
All lookup entities extend BaseLookupEntity:
```java
- id: Integer
- name: String
```

## API Documentation

### Task Operations
- GET `/api/v1/task/{id}` - Get task by ID
- GET `/api/v1/task` - Get all tasks (paginated)
- POST `/api/v1/task` - Create task
- PUT `/api/v1/task/{id}` - Update task
- DELETE `/api/v1/task/{id}` - Delete task

### Issue Operations
- GET `/api/v1/issue/{id}` - Get issue by ID
- POST `/api/v1/issue` - Create issue
- PUT `/api/v1/issue/{id}` - Update issue
- DELETE `/api/v1/issue/{id}` - Delete issue

### Lookup Operations
- GET `/api/v1/lookup/task-status` - Get task statuses
- GET `/api/v1/lookup/task-priority` - Get task priorities
- GET `/api/v1/lookup/issue-status` - Get issue statuses
- GET `/api/v1/lookup/issue-criticality` - Get issue criticality levels

Swagger UI available at: `http://localhost:8080/swagger-ui.html`

## Setup

### Prerequisites
- Docker
- Docker Compose

### Local Development
```bash
# Clone repository
git clone https://github.com/Faisal-Alagla/task-manager.git

# Start services
docker-compose up
```

### Environment Variables
```
ENV=dev
DB_URL=jdbc:postgresql://db:5432/task-manager
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

## Key Design Decisions
- Closure table pattern for efficient hierarchical task querying
- AOP-based activity logging
- Centralized exception handling
- External user management through separate microservice
- Security handled by API Gateway
