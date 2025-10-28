package com.faisal.taskmanager.issue;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

/**
 * data container for lookup IDs and mappings.
 */
@Getter
@Builder
public class IssueLookupContext {

    // Status IDs
    private Set<Integer> issueStatusIds;

    // Criticality IDs
    private Set<Integer> issueCriticalityIds;

}
