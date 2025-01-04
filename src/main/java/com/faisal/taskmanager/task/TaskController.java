package com.faisal.taskmanager.task;

import com.faisal.taskmanager.utils.constants.BaseRoutingConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//TODO: add swagger documentation for controller and endpoints
@RestController
@RequestMapping(path = BaseRoutingConstants.API + BaseRoutingConstants.V1 + BaseRoutingConstants.task)
@RequiredArgsConstructor
public class TaskController {

    private final ITaskService iTaskService;

    @PostMapping()
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskCreationDto taskCreationDto) {
        TaskResponseDto taskResponseDto = iTaskService.createTask(taskCreationDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable("id") UUID id) {
        TaskResponseDto taskResponseDto = iTaskService.getTask(id);

        return ResponseEntity.status(HttpStatus.OK).body(taskResponseDto);
    }

    //TODO: make it pageable
    @GetMapping()
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<TaskResponseDto> taskResponseDtos = iTaskService.getAllTasks();

        return ResponseEntity.status(HttpStatus.OK).body(taskResponseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable("id") UUID id,
            @Valid @RequestBody TaskUpdateDto taskUpdateDto
    ) {
        TaskResponseDto taskResponseDto = iTaskService.updateTask(taskUpdateDto, id);

        return ResponseEntity.status(HttpStatus.OK).body(taskResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatusCode> deleteTask(@PathVariable("id") UUID id) {
        iTaskService.deleteTask(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
