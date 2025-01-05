package com.faisal.taskmanager.issue;

import com.faisal.taskmanager.utils.constants.BaseRoutingConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(
        name = "Issue Operations",
        description = "Endpoints for Issue operations"
)
@RestController
@RequestMapping(path = BaseRoutingConstants.API + BaseRoutingConstants.V1 + BaseRoutingConstants.ISSUE)
@RequiredArgsConstructor
@Validated
public class IssueController {

    private final IIssueService iIssueService;

    @Operation(
            summary = "Create Issue",
            description = "Add a new issue to a specific task"
    )
    @PostMapping()
    public ResponseEntity<IssueResponseDto> createIssue(@Valid @RequestBody IssueCreationDto issueCreationDto) {
        IssueResponseDto issueResponseDto = iIssueService.createIssue(issueCreationDto);

        return new ResponseEntity<>(issueResponseDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Issue",
            description = "Get a specific issue by issue id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<IssueResponseDto> getIssue(@PathVariable("id") UUID id) {
        IssueResponseDto issueResponseDto = iIssueService.getIssue(id);

        return new ResponseEntity<>(issueResponseDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Issue",
            description = "Delete a specific issue by issue id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatusCode> deleteIssue(@PathVariable("id") UUID id) {
        iIssueService.deleteIssue(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
