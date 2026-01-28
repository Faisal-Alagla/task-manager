package com.faisal.taskmanager.issue.validator;

import com.faisal.taskmanager.common.lookups.LookupService;
import com.faisal.taskmanager.common.lookups.domain.IssueCriticalityLookupCollection;
import com.faisal.taskmanager.common.lookups.domain.IssueStatusLookupCollection;
import com.faisal.taskmanager.common.lookups.entities.IssueCriticalityLk;
import com.faisal.taskmanager.common.lookups.entities.IssueStatusLk;
import com.faisal.taskmanager.issue.IssueLookupContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

/**
 * Configuration class for creating IssueLookupContext bean.
 *
 * <p>Extracts lookup IDs from collections and provides them as an immutable context
 * for validation and business logic operations.</p>
 *
 * @author Faisal
 */
@Configuration
@RequiredArgsConstructor
public class IssueLookupContextConfig {

    private final LookupService lookupService;

    /**
     * Creates IssueLookupContext bean with pre-extracted lookup IDs.
     *
     * @return immutable IssueLookupContext for validation operations
     */
    @Bean
    public IssueLookupContext issueLookupContext() {
        IssueCriticalityLookupCollection criticalities = lookupService.getIssueCriticalityCollection();
        IssueStatusLookupCollection statuses = lookupService.getIssueStatusCollection();

        return IssueLookupContext.builder()
                // Issue status IDs
                .issueStatusIds(
                        statuses.stream()
                                .map(IssueStatusLk::getId)
                                .collect(Collectors.toSet())
                )

                // Issue criticality IDs
                .issueCriticalityIds(
                        criticalities.stream()
                                .map(IssueCriticalityLk::getId)
                                .collect(Collectors.toSet())
                )

                .build();
    }
}
