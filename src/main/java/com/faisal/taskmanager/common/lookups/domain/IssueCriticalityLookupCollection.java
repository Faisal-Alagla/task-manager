package com.faisal.taskmanager.common.lookups.domain;

import com.faisal.taskmanager.common.lookups.entities.IssueCriticalityLk;
import com.faisal.taskmanager.common.lookups.enums.IssueCriticalityEnum;
import com.faisal.taskmanager.common.lookups.specifications.IssueCriticalitySpecifications;

import java.util.List;

public class IssueCriticalityLookupCollection extends BaseLookupCollection<IssueCriticalityLk> {

    public IssueCriticalityLookupCollection(List<IssueCriticalityLk> items) {
        super(items);
    }

    /**
     * Convert entity ID to enum
     */
    public IssueCriticalityEnum toEnum(Integer criticalityId) {
        return IssueCriticalityEnum.fromId(criticalityId).orElse(null);
    }

    /**
     * Get entity by enum
     */
    public IssueCriticalityLk getByEnum(IssueCriticalityEnum criticalityEnum) {
        return findById(criticalityEnum.getId());
    }

    /**
     * Get high criticality levels
     */
    public List<IssueCriticalityLk> getHighCriticalities() {
        return findBy(IssueCriticalitySpecifications.isHigh());
    }

    /**
     * Get low criticality levels
     */
    public List<IssueCriticalityLk> getLowCriticalities() {
        return findBy(IssueCriticalitySpecifications.isLow());
    }

    /**
     * Get medium criticality levels
     */
    public List<IssueCriticalityLk> getMediumCriticalities() {
        return findBy(IssueCriticalitySpecifications.isMedium());
    }

    /**
     * Check if criticality is high
     */
    public boolean isHigh(Integer criticalityId) {
        return IssueCriticalityEnum.fromId(criticalityId)
                .map(IssueCriticalityEnum::isHigh)
                .orElse(false);
    }

    /**
     * Check if criticality is low
     */
    public boolean isLow(Integer criticalityId) {
        return IssueCriticalityEnum.fromId(criticalityId)
                .map(IssueCriticalityEnum::isLow)
                .orElse(false);
    }

    /**
     * Check if criticality requires immediate attention
     */
    public boolean requiresImmediateAttention(Integer criticalityId) {
        return IssueCriticalityEnum.fromId(criticalityId)
                .map(IssueCriticalityEnum::requiresImmediateAttention)
                .orElse(false);
    }

    /**
     * Get severity level (1-3, higher is more severe)
     */
    public int getSeverityLevel(Integer criticalityId) {
        return IssueCriticalityEnum.fromId(criticalityId)
                .map(IssueCriticalityEnum::getSeverityLevel)
                .orElse(0);
    }
}