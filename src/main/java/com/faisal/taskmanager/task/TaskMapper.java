package com.faisal.taskmanager.task;


public class TaskMapper {

    /**
     * maps a Task object to TaskResponseDto
     *
     * @param task The Task object
     * @return TaskResponseDto object containing the passed Task's data
     */
    public static TaskResponseDto mapToTaskResponseDto(Task task) {
        return TaskResponseDto.builder()
                .id(task.getId())
                .assigneeId(task.getAssigneeId())
                .name(task.getName())
                .dueDate(task.getDueDate())
                .description(task.getDescription())
                .statusId(task.getStatusId())
                .priorityId(task.getPriorityId())
                .build();
    }

    /**
     * maps a TaskCreationDto object to Task
     *
     * @param taskCreationDto The TaskCreationDto object
     * @return Task object containing the passed TaskCreationDto data
     */
    public static Task mapToTask(TaskCreationDto taskCreationDto) {
        return Task.builder()
                .name(taskCreationDto.getName())
                .assigneeId(taskCreationDto.getAssigneeId())
                .dueDate(taskCreationDto.getDueDate())
                .description(taskCreationDto.getDescription())
                .statusId(taskCreationDto.getStatusId())
                .priorityId(taskCreationDto.getPriorityId())
                .build();
    }

}
