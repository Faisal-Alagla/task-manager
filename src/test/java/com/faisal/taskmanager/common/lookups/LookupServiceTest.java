package com.faisal.taskmanager.common.lookups;

import com.faisal.taskmanager.common.lookups.domain.IssueCriticalityLookupCollection;
import com.faisal.taskmanager.common.lookups.domain.IssueStatusLookupCollection;
import com.faisal.taskmanager.common.lookups.domain.TaskPriorityLookupCollection;
import com.faisal.taskmanager.common.lookups.domain.TaskStatusLookupCollection;
import com.faisal.taskmanager.common.lookups.entities.IssueCriticalityLk;
import com.faisal.taskmanager.common.lookups.entities.IssueStatusLk;
import com.faisal.taskmanager.common.lookups.entities.TaskPriorityLk;
import com.faisal.taskmanager.common.lookups.entities.TaskStatusLk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.faisal.taskmanager.testutils.fixtures.MockLookupFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LookupService Tests")
class LookupServiceTest {

    @Mock
    private LookupRepository lookupRepository;

    @InjectMocks
    private LookupService lookupService;

    @BeforeEach
    void setUp() {
        when(lookupRepository.getTaskStatusLookup()).thenReturn(createAllTaskStatuses());
        when(lookupRepository.getTaskPriorityLookup()).thenReturn(createAllTaskPriorities());
        when(lookupRepository.getIssueStatusLookup()).thenReturn(createAllIssueStatuses());
        when(lookupRepository.getIssueCriticalityLookup()).thenReturn(createAllIssueCriticalities());

        lookupService = new LookupService(lookupRepository);
        lookupService.initializeLookups();
    }

    @Nested
    @DisplayName("Initialization Tests")
    class InitializationTests {

        @Test
        @DisplayName("Should load task status collection")
        void init_ShouldLoadTaskStatusCollection() {
            assertThat(lookupService.getTaskStatusCollection()).isNotNull();
        }

        @Test
        @DisplayName("Should load task priority collection")
        void init_ShouldLoadTaskPriorityCollection() {
            assertThat(lookupService.getTaskPriorityCollection()).isNotNull();
        }

        @Test
        @DisplayName("Should load issue status collection")
        void init_ShouldLoadIssueStatusCollection() {
            assertThat(lookupService.getIssueStatusCollection()).isNotNull();
        }

        @Test
        @DisplayName("Should load issue criticality collection")
        void init_ShouldLoadIssueCriticalityCollection() {
            assertThat(lookupService.getIssueCriticalityCollection()).isNotNull();
        }

        @Test
        @DisplayName("Should cache task statuses with correct count")
        void init_ShouldCacheTaskStatusesWithCorrectCount() {
            TaskStatusLookupCollection collection = lookupService.getTaskStatusCollection();

            assertThat(collection.getAll()).hasSize(4);
        }

        @Test
        @DisplayName("Should cache task statuses with correct IDs")
        void init_ShouldCacheTaskStatusesWithCorrectIds() {
            TaskStatusLookupCollection collection = lookupService.getTaskStatusCollection();

            assertThat(collection.getAll())
                    .extracting(TaskStatusLk::getId)
                    .containsExactlyInAnyOrder(0, 1, 2, 3);
        }

        @Test
        @DisplayName("Should cache task priorities with correct count")
        void init_ShouldCacheTaskPrioritiesWithCorrectCount() {
            TaskPriorityLookupCollection collection = lookupService.getTaskPriorityCollection();

            assertThat(collection.getAll()).hasSize(3);
        }

        @Test
        @DisplayName("Should cache task priorities with correct IDs")
        void init_ShouldCacheTaskPrioritiesWithCorrectIds() {
            TaskPriorityLookupCollection collection = lookupService.getTaskPriorityCollection();

            assertThat(collection.getAll())
                    .extracting(TaskPriorityLk::getId)
                    .containsExactlyInAnyOrder(0, 1, 2);
        }

        @Test
        @DisplayName("Should cache issue statuses with correct count")
        void init_ShouldCacheIssueStatusesWithCorrectCount() {
            IssueStatusLookupCollection collection = lookupService.getIssueStatusCollection();

            assertThat(collection.getAll()).hasSize(2);
        }

        @Test
        @DisplayName("Should cache issue statuses with correct IDs")
        void init_ShouldCacheIssueStatusesWithCorrectIds() {
            IssueStatusLookupCollection collection = lookupService.getIssueStatusCollection();

            assertThat(collection.getAll())
                    .extracting(IssueStatusLk::getId)
                    .containsExactlyInAnyOrder(0, 1);
        }

        @Test
        @DisplayName("Should cache issue criticalities with correct count")
        void init_ShouldCacheIssueCriticalitiesWithCorrectCount() {
            IssueCriticalityLookupCollection collection = lookupService.getIssueCriticalityCollection();

            assertThat(collection.getAll()).hasSize(3);
        }

        @Test
        @DisplayName("Should cache issue criticalities with correct IDs")
        void init_ShouldCacheIssueCriticalitiesWithCorrectIds() {
            IssueCriticalityLookupCollection collection = lookupService.getIssueCriticalityCollection();

            assertThat(collection.getAll())
                    .extracting(IssueCriticalityLk::getId)
                    .containsExactlyInAnyOrder(0, 1, 2);
        }
    }

    @Nested
    @DisplayName("Collection Retrieval Tests")
    class CollectionRetrievalTests {

        @Test
        @DisplayName("Should return same task status collection instance")
        void getTaskStatusCollection_ShouldReturnCachedCollection() {
            TaskStatusLookupCollection collection1 = lookupService.getTaskStatusCollection();
            TaskStatusLookupCollection collection2 = lookupService.getTaskStatusCollection();

            assertThat(collection1).isSameAs(collection2);
        }

        @Test
        @DisplayName("Should return same task priority collection instance")
        void getTaskPriorityCollection_ShouldReturnCachedCollection() {
            TaskPriorityLookupCollection collection1 = lookupService.getTaskPriorityCollection();
            TaskPriorityLookupCollection collection2 = lookupService.getTaskPriorityCollection();

            assertThat(collection1).isSameAs(collection2);
        }

        @Test
        @DisplayName("Should return same issue status collection instance")
        void getIssueStatusCollection_ShouldReturnCachedCollection() {
            IssueStatusLookupCollection collection1 = lookupService.getIssueStatusCollection();
            IssueStatusLookupCollection collection2 = lookupService.getIssueStatusCollection();

            assertThat(collection1).isSameAs(collection2);
        }

        @Test
        @DisplayName("Should return same issue criticality collection instance")
        void getIssueCriticalityCollection_ShouldReturnCachedCollection() {
            IssueCriticalityLookupCollection collection1 = lookupService.getIssueCriticalityCollection();
            IssueCriticalityLookupCollection collection2 = lookupService.getIssueCriticalityCollection();

            assertThat(collection1).isSameAs(collection2);
        }
    }

    @Nested
    @DisplayName("Collection Content Tests")
    class CollectionContentTests {

        @Test
        @DisplayName("Task status collection should have terminal statuses")
        void taskStatusCollection_ShouldHaveTerminalStatuses() {
            TaskStatusLookupCollection collection = lookupService.getTaskStatusCollection();
            List<TaskStatusLk> terminalStatuses = collection.getTerminalStatuses();

            assertThat(terminalStatuses).hasSize(2);
        }

        @Test
        @DisplayName("Task status collection should have correct terminal status IDs")
        void taskStatusCollection_ShouldHaveCorrectTerminalStatusIds() {
            TaskStatusLookupCollection collection = lookupService.getTaskStatusCollection();
            List<TaskStatusLk> terminalStatuses = collection.getTerminalStatuses();

            assertThat(terminalStatuses)
                    .extracting(TaskStatusLk::getId)
                    .containsExactlyInAnyOrder(1, 2);
        }

        @Test
        @DisplayName("Should find task status by ID")
        void taskStatusCollection_ShouldFindById() {
            TaskStatusLookupCollection collection = lookupService.getTaskStatusCollection();
            TaskStatusLk status = collection.findById(0);

            assertThat(status).isNotNull();
        }

        @Test
        @DisplayName("Should find task status with correct ID")
        void taskStatusCollection_ShouldFindByIdWithCorrectId() {
            TaskStatusLookupCollection collection = lookupService.getTaskStatusCollection();
            TaskStatusLk status = collection.findById(0);

            assertThat(status.getId()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should find task status with correct name")
        void taskStatusCollection_ShouldFindByIdWithCorrectName() {
            TaskStatusLookupCollection collection = lookupService.getTaskStatusCollection();
            TaskStatusLk status = collection.findById(0);

            assertThat(status.getName()).isEqualTo("in_progress");
        }

        @Test
        @DisplayName("Should find task priority by ID")
        void taskPriorityCollection_ShouldFindById() {
            TaskPriorityLookupCollection collection = lookupService.getTaskPriorityCollection();
            TaskPriorityLk priority = collection.findById(1);

            assertThat(priority).isNotNull();
        }

        @Test
        @DisplayName("Should find task priority with correct ID")
        void taskPriorityCollection_ShouldFindByIdWithCorrectId() {
            TaskPriorityLookupCollection collection = lookupService.getTaskPriorityCollection();
            TaskPriorityLk priority = collection.findById(1);

            assertThat(priority.getId()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should find task priority with correct name")
        void taskPriorityCollection_ShouldFindByIdWithCorrectName() {
            TaskPriorityLookupCollection collection = lookupService.getTaskPriorityCollection();
            TaskPriorityLk priority = collection.findById(1);

            assertThat(priority.getName()).isEqualTo("medium");
        }

        @Test
        @DisplayName("Should find issue status by ID")
        void issueStatusCollection_ShouldFindById() {
            IssueStatusLookupCollection collection = lookupService.getIssueStatusCollection();
            IssueStatusLk status = collection.findById(1);

            assertThat(status).isNotNull();
        }

        @Test
        @DisplayName("Should find issue status with correct ID")
        void issueStatusCollection_ShouldFindByIdWithCorrectId() {
            IssueStatusLookupCollection collection = lookupService.getIssueStatusCollection();
            IssueStatusLk status = collection.findById(1);

            assertThat(status.getId()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should find issue status with correct name")
        void issueStatusCollection_ShouldFindByIdWithCorrectName() {
            IssueStatusLookupCollection collection = lookupService.getIssueStatusCollection();
            IssueStatusLk status = collection.findById(1);

            assertThat(status.getName()).isEqualTo("resolved");
        }

        @Test
        @DisplayName("Should find issue criticality by ID")
        void issueCriticalityCollection_ShouldFindById() {
            IssueCriticalityLookupCollection collection = lookupService.getIssueCriticalityCollection();
            IssueCriticalityLk criticality = collection.findById(2);

            assertThat(criticality).isNotNull();
        }

        @Test
        @DisplayName("Should find issue criticality with correct ID")
        void issueCriticalityCollection_ShouldFindByIdWithCorrectId() {
            IssueCriticalityLookupCollection collection = lookupService.getIssueCriticalityCollection();
            IssueCriticalityLk criticality = collection.findById(2);

            assertThat(criticality.getId()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should find issue criticality with correct name")
        void issueCriticalityCollection_ShouldFindByIdWithCorrectName() {
            IssueCriticalityLookupCollection collection = lookupService.getIssueCriticalityCollection();
            IssueCriticalityLk criticality = collection.findById(2);

            assertThat(criticality.getName()).isEqualTo("low");
        }
    }

    @Nested
    @DisplayName("Controller Endpoint Tests")
    class ControllerEndpointTests {

        @Test
        @DisplayName("Should return issue criticalities as DTOs")
        void getIssueCriticalities_ShouldReturnDtoList() {
            List<LookupResponseDto> result = lookupService.getIssueCriticalities();

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should return issue criticalities with correct count")
        void getIssueCriticalities_ShouldReturnCorrectCount() {
            List<LookupResponseDto> result = lookupService.getIssueCriticalities();

            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("Should return issue criticalities with correct IDs")
        void getIssueCriticalities_ShouldReturnCorrectIds() {
            List<LookupResponseDto> result = lookupService.getIssueCriticalities();

            assertThat(result)
                    .extracting(LookupResponseDto::getId)
                    .containsExactlyInAnyOrder(0, 1, 2);
        }

        @Test
        @DisplayName("Should return issue statuses as DTOs")
        void getIssueStatuses_ShouldReturnDtoList() {
            List<LookupResponseDto> result = lookupService.getIssueStatuses();

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should return issue statuses with correct count")
        void getIssueStatuses_ShouldReturnCorrectCount() {
            List<LookupResponseDto> result = lookupService.getIssueStatuses();

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("Should return issue statuses with correct IDs")
        void getIssueStatuses_ShouldReturnCorrectIds() {
            List<LookupResponseDto> result = lookupService.getIssueStatuses();

            assertThat(result)
                    .extracting(LookupResponseDto::getId)
                    .containsExactlyInAnyOrder(0, 1);
        }

        @Test
        @DisplayName("Should return task priorities as DTOs")
        void getTaskPriorities_ShouldReturnDtoList() {
            List<LookupResponseDto> result = lookupService.getTaskPriorities();

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should return task priorities with correct count")
        void getTaskPriorities_ShouldReturnCorrectCount() {
            List<LookupResponseDto> result = lookupService.getTaskPriorities();

            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("Should return task priorities with correct IDs")
        void getTaskPriorities_ShouldReturnCorrectIds() {
            List<LookupResponseDto> result = lookupService.getTaskPriorities();

            assertThat(result)
                    .extracting(LookupResponseDto::getId)
                    .containsExactlyInAnyOrder(0, 1, 2);
        }

        @Test
        @DisplayName("Should return task statuses as DTOs")
        void getTaskStatuses_ShouldReturnDtoList() {
            List<LookupResponseDto> result = lookupService.getTaskStatuses();

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should return task statuses with correct count")
        void getTaskStatuses_ShouldReturnCorrectCount() {
            List<LookupResponseDto> result = lookupService.getTaskStatuses();

            assertThat(result).hasSize(4);
        }

        @Test
        @DisplayName("Should return task statuses with correct IDs")
        void getTaskStatuses_ShouldReturnCorrectIds() {
            List<LookupResponseDto> result = lookupService.getTaskStatuses();

            assertThat(result)
                    .extracting(LookupResponseDto::getId)
                    .containsExactlyInAnyOrder(0, 1, 2, 3);
        }
    }
}
