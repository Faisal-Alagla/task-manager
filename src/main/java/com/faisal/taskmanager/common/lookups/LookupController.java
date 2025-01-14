package com.faisal.taskmanager.common.lookups;

import com.faisal.taskmanager.utils.annotations.NoActivityLogging;
import com.faisal.taskmanager.utils.constants.BaseRoutingConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Lookup Operations",
        description = "Endpoints for Lookup operations"
)
@RestController
@RequestMapping(BaseRoutingConstants.API + BaseRoutingConstants.V1 + BaseRoutingConstants.LOOKUP)
@RequiredArgsConstructor
@NoActivityLogging
public class LookupController {

    private final LookupService lookupService;

    @Operation(
            summary = "Issue Criticality Lookup",
            description = "Get all lookups for issue criticality types"
    )
    @GetMapping("/issue-criticality")
    ResponseEntity<List<LookupResponseDto>> getIssueCriticalityLookup() {

        List<LookupResponseDto> issueCriticality = lookupService.getLookup(LookupType.ISSUE_CRITICALITY).toList();
        return new ResponseEntity<>(issueCriticality, HttpStatus.OK);
    }

    @Operation(
            summary = "Issue Status Lookup",
            description = "Get all lookups for issue status types"
    )
    @GetMapping("/issue-status")
    ResponseEntity<List<LookupResponseDto>> getIssueStatusLookup() {

        List<LookupResponseDto> issueStatus = lookupService.getLookup(LookupType.ISSUE_STATUS).toList();
        return new ResponseEntity<>(issueStatus, HttpStatus.OK);
    }

    @Operation(
            summary = "Task Priority Lookup",
            description = "Get all lookups for task priority types"
    )
    @GetMapping("/task-priority")
    ResponseEntity<List<LookupResponseDto>> getTaskPriorityLookup() {

        List<LookupResponseDto> taskPriority = lookupService.getLookup(LookupType.TASK_PRIORITY).toList();
        return new ResponseEntity<>(taskPriority, HttpStatus.OK);
    }

    @Operation(
            summary = "Task Status Lookup",
            description = "Get all lookups for task status types"
    )
    @GetMapping("/task-status")
    ResponseEntity<List<LookupResponseDto>> getTaskStatusLookup() {

        List<LookupResponseDto> taskStatus = lookupService.getLookup(LookupType.TASK_STATUS).toList();
        return new ResponseEntity<>(taskStatus, HttpStatus.OK);
    }
}
