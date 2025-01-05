package com.faisal.taskmanager.task;

import com.faisal.taskmanager.utils.constants.BaseRoutingConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(
        name = "Task Operations",
        description = "Endpoints for Task operations"
)
@RestController
@RequestMapping(path = BaseRoutingConstants.API + BaseRoutingConstants.V1 + BaseRoutingConstants.TASK)
@RequiredArgsConstructor
public class TaskController {

    private final ITaskService iTaskService;

    @Operation(
            summary = "Create Task",
            description = "Create a new task"
    )
    @PostMapping()
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskCreationDto taskCreationDto) {
        TaskResponseDto taskResponseDto = iTaskService.createTask(taskCreationDto);

        return new ResponseEntity<>(taskResponseDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Task",
            description = "Get a specific task by task id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable("id") UUID id) {
        TaskResponseDto taskResponseDto = iTaskService.getTask(id);

        return new ResponseEntity<>(taskResponseDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Get All Tasks",
            description = "Get all tasks"
    )
    @GetMapping()
    public ResponseEntity<Page<TaskResponseDto>> getAllTasks(Pageable pageable) {
        Page<TaskResponseDto> taskResponseDtos = iTaskService.getAllTasks(pageable);

        return new ResponseEntity<>(taskResponseDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Update Task",
            description = "Update a specific task by task id"
    )
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable("id") UUID id,
            @Valid @RequestBody TaskUpdateDto taskUpdateDto
    ) {
        TaskResponseDto taskResponseDto = iTaskService.updateTask(taskUpdateDto, id);

        return new ResponseEntity<>(taskResponseDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Task",
            description = "Delete a specific task by task id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatusCode> deleteTask(@PathVariable("id") UUID id) {
        iTaskService.deleteTask(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
