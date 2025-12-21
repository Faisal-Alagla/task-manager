package com.faisal.taskmanager.common.lookups.specifications;

import com.faisal.taskmanager.common.lookups.BaseLookupEntity;

import java.util.function.Predicate;

/**
 * Specification pattern for lookup filtering
 */
@FunctionalInterface
public interface LookupSpecification<T extends BaseLookupEntity> {

    /**
     * Convert specification to predicate
     */
    Predicate<T> toPredicate();

    /**
     * Combine this specification with another using AND
     */
    default LookupSpecification<T> and(LookupSpecification<T> other) {
        return () -> toPredicate().and(other.toPredicate());
    }

    /**
     * Combine this specification with another using OR
     */
    default LookupSpecification<T> or(LookupSpecification<T> other) {
        return () -> toPredicate().or(other.toPredicate());
    }

    /**
     * Negate this specification
     */
    default LookupSpecification<T> not() {
        return () -> toPredicate().negate();
    }
}