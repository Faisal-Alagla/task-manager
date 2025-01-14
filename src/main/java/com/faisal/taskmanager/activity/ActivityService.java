package com.faisal.taskmanager.activity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    void logActivity(ActivityRequestDto activityRequestDto) {

        Activity activity = Activity.builder()
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .isActive(true)
                .httpMethod(activityRequestDto.getHttpMethod())
                .uri(activityRequestDto.getUri())
                .operation(activityRequestDto.getOperation())
                .operationReferenceId(activityRequestDto.getOperationReferenceId())
                .message(activityRequestDto.getMessage())
                .userId(activityRequestDto.getUserId())
                .build();

        activityRepository.save(activity);
    }

}
