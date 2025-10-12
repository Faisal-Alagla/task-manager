package com.faisal.taskmanager.common.lookups.specifications;

import com.faisal.taskmanager.common.lookups.entities.IssueCriticalityLk;
import com.faisal.taskmanager.common.lookups.enums.IssueCriticalityEnum;

public class IssueCriticalitySpecifications {

    /**
     * Specification for high criticality
     */
    public static LookupSpecification<IssueCriticalityLk> isHigh() {
        return () -> criticality -> IssueCriticalityEnum.fromId(criticality.getId())
                    .map(IssueCriticalityEnum::isHigh)
                    .orElse(false);
    }

    /**
     * Specification for low criticality
     */
    public static LookupSpecification<IssueCriticalityLk> isLow() {
        return () -> criticality -> IssueCriticalityEnum.fromId(criticality.getId())
                    .map(IssueCriticalityEnum::isLow)
                    .orElse(false);
    }

    /**
     * Specification for medium criticality
     */
    public static LookupSpecification<IssueCriticalityLk> isMedium() {
        return () -> criticality -> IssueCriticalityEnum.fromId(criticality.getId())
                    .map(e -> e == IssueCriticalityEnum.MEDIUM)
                    .orElse(false);
    }

    /**
     * Specification for issues requiring immediate attention
     */
    public static LookupSpecification<IssueCriticalityLk> requiresImmediateAttention() {
        return () -> criticality -> IssueCriticalityEnum.fromId(criticality.getId())
                    .map(IssueCriticalityEnum::requiresImmediateAttention)
                    .orElse(false);
    }
}