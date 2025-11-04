package com.faisal.taskmanager.activity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static com.faisal.taskmanager.testutils.constants.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ActivityService Tests")
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;

    @Nested
    @DisplayName("Activity Logging Tests")
    class ActivityLoggingTests {

        @Test
        @DisplayName("Should call repository save")
        void logActivity_WithValidRequest_ShouldCallSave() {
            ActivityRequestDto requestDto = new ActivityRequestDto(
                    HTTP_METHOD_POST,
                    API_PATH_TASKS,
                    OPERATION_CREATE,
                    TASK_ID_1.toString(),
                    "Created new task",
                    USER_ID_1
            );

            Activity savedActivity = Activity.builder().build();
            when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

            activityService.logActivity(requestDto);

            verify(activityRepository).save(any(Activity.class));
        }

        @Test
        @DisplayName("Should map HTTP method correctly")
        void logActivity_ShouldMapHttpMethod() {
            ActivityRequestDto requestDto = new ActivityRequestDto(
                    HTTP_METHOD_PUT,
                    API_PATH_ISSUES,
                    OPERATION_UPDATE,
                    ISSUE_ID_1.toString(),
                    "Updated issue",
                    USER_ID_2
            );

            Activity savedActivity = Activity.builder().build();
            when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

            activityService.logActivity(requestDto);

            ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);
            verify(activityRepository).save(activityCaptor.capture());
            assertThat(activityCaptor.getValue().getHttpMethod()).isEqualTo(HTTP_METHOD_PUT);
        }

        @Test
        @DisplayName("Should map URI correctly")
        void logActivity_ShouldMapUri() {
            ActivityRequestDto requestDto = new ActivityRequestDto(
                    HTTP_METHOD_PUT,
                    API_PATH_ISSUES,
                    OPERATION_UPDATE,
                    ISSUE_ID_1.toString(),
                    "Updated issue",
                    USER_ID_2
            );

            Activity savedActivity = Activity.builder().build();
            when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

            activityService.logActivity(requestDto);

            ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);
            verify(activityRepository).save(activityCaptor.capture());
            assertThat(activityCaptor.getValue().getUri()).isEqualTo(API_PATH_ISSUES);
        }

        @Test
        @DisplayName("Should map operation correctly")
        void logActivity_ShouldMapOperation() {
            ActivityRequestDto requestDto = new ActivityRequestDto(
                    HTTP_METHOD_PUT,
                    API_PATH_ISSUES,
                    OPERATION_UPDATE,
                    ISSUE_ID_1.toString(),
                    "Updated issue",
                    USER_ID_2
            );

            Activity savedActivity = Activity.builder().build();
            when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

            activityService.logActivity(requestDto);

            ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);
            verify(activityRepository).save(activityCaptor.capture());
            assertThat(activityCaptor.getValue().getOperation()).isEqualTo(OPERATION_UPDATE);
        }

        @Test
        @DisplayName("Should map operation reference ID correctly")
        void logActivity_ShouldMapOperationReferenceId() {
            ActivityRequestDto requestDto = new ActivityRequestDto(
                    HTTP_METHOD_PUT,
                    API_PATH_ISSUES,
                    OPERATION_UPDATE,
                    ISSUE_ID_1.toString(),
                    "Updated issue",
                    USER_ID_2
            );

            Activity savedActivity = Activity.builder().build();
            when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

            activityService.logActivity(requestDto);

            ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);
            verify(activityRepository).save(activityCaptor.capture());
            assertThat(activityCaptor.getValue().getOperationReferenceId()).isEqualTo(ISSUE_ID_1.toString());
        }

        @Test
        @DisplayName("Should map message correctly")
        void logActivity_ShouldMapMessage() {
            ActivityRequestDto requestDto = new ActivityRequestDto(
                    HTTP_METHOD_PUT,
                    API_PATH_ISSUES,
                    OPERATION_UPDATE,
                    ISSUE_ID_1.toString(),
                    "Updated issue",
                    USER_ID_2
            );

            Activity savedActivity = Activity.builder().build();
            when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

            activityService.logActivity(requestDto);

            ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);
            verify(activityRepository).save(activityCaptor.capture());
            assertThat(activityCaptor.getValue().getMessage()).isEqualTo("Updated issue");
        }

        @Test
        @DisplayName("Should map user ID correctly")
        void logActivity_ShouldMapUserId() {
            ActivityRequestDto requestDto = new ActivityRequestDto(
                    HTTP_METHOD_PUT,
                    API_PATH_ISSUES,
                    OPERATION_UPDATE,
                    ISSUE_ID_1.toString(),
                    "Updated issue",
                    USER_ID_2
            );

            Activity savedActivity = Activity.builder().build();
            when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

            activityService.logActivity(requestDto);

            ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);
            verify(activityRepository).save(activityCaptor.capture());
            assertThat(activityCaptor.getValue().getUserId()).isEqualTo(USER_ID_2);
        }

        @Test
        @DisplayName("Should handle null user ID")
        void logActivity_ShouldHandleNullUserId() {
            ActivityRequestDto requestDto = new ActivityRequestDto(
                    HTTP_METHOD_GET,
                    API_PATH_TASKS,
                    OPERATION_READ,
                    TASK_ID_1.toString(),
                    null,
                    null
            );

            Activity savedActivity = Activity.builder().build();
            when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

            activityService.logActivity(requestDto);

            ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);
            verify(activityRepository).save(activityCaptor.capture());
            assertThat(activityCaptor.getValue().getUserId()).isNull();
        }

        @Test
        @DisplayName("Should set createdAt timestamp")
        void logActivity_ShouldSetCreatedAt() {
            ActivityRequestDto requestDto = new ActivityRequestDto(
                    HTTP_METHOD_DELETE,
                    API_PATH_TASKS,
                    OPERATION_DELETE,
                    TASK_ID_1.toString(),
                    "Deleted task",
                    USER_ID_1
            );

            Activity savedActivity = Activity.builder().build();
            when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

            Instant beforeLog = Instant.now();
            activityService.logActivity(requestDto);
            Instant afterLog = Instant.now();

            ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);
            verify(activityRepository).save(activityCaptor.capture());
            assertThat(activityCaptor.getValue().getCreatedAt()).isBetween(beforeLog, afterLog);
        }

        @Test
        @DisplayName("Should set updatedAt timestamp")
        void logActivity_ShouldSetUpdatedAt() {
            ActivityRequestDto requestDto = new ActivityRequestDto(
                    HTTP_METHOD_DELETE,
                    API_PATH_TASKS,
                    OPERATION_DELETE,
                    TASK_ID_1.toString(),
                    "Deleted task",
                    USER_ID_1
            );

            Activity savedActivity = Activity.builder().build();
            when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

            Instant beforeLog = Instant.now();
            activityService.logActivity(requestDto);
            Instant afterLog = Instant.now();

            ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);
            verify(activityRepository).save(activityCaptor.capture());
            assertThat(activityCaptor.getValue().getUpdatedAt()).isBetween(beforeLog, afterLog);
        }

        @Test
        @DisplayName("Should set isActive to true")
        void logActivity_ShouldSetIsActiveTrue() {
            ActivityRequestDto requestDto = new ActivityRequestDto(
                    HTTP_METHOD_POST,
                    API_PATH_TASKS,
                    OPERATION_CREATE,
                    TASK_ID_1.toString(),
                    "Created task",
                    USER_ID_1
            );

            Activity savedActivity = Activity.builder().build();
            when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

            activityService.logActivity(requestDto);

            ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);
            verify(activityRepository).save(activityCaptor.capture());
            assertThat(activityCaptor.getValue().getIsActive()).isTrue();
        }
    }
}
