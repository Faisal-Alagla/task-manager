package com.faisal.taskmanager.issue;

import com.faisal.taskmanager.utils.constants.BaseRoutingConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

//TODO: add swagger documentation for controller and endpoints
@RestController
@RequestMapping(path = BaseRoutingConstants.API + BaseRoutingConstants.V1 + BaseRoutingConstants.issue)
@RequiredArgsConstructor
public class IssueController {

    private final IIssueService iIssueService;

    @PostMapping()
    public ResponseEntity<IssueResponseDto> createIssue(@Valid @RequestBody IssueCreationDto issueCreationDto) {
        IssueResponseDto issueResponseDto = iIssueService.createIssue(issueCreationDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(issueResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueResponseDto> getIssue(@PathVariable("id") UUID id) {
        IssueResponseDto issueResponseDto = iIssueService.getIssue(id);

        return ResponseEntity.status(HttpStatus.OK).body(issueResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatusCode> deleteIssue(@PathVariable("id") UUID id) {
        iIssueService.deleteIssue(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
