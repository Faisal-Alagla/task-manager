package com.faisal.taskmanager.common.lookups.domain;

import com.faisal.taskmanager.common.lookups.BaseLookupEntity;
import com.faisal.taskmanager.common.lookups.LookupResponseDto;
import com.faisal.taskmanager.common.lookups.specifications.LookupSpecification;

import java.util.List;
import java.util.stream.Stream;

public abstract class BaseLookupCollection<T extends BaseLookupEntity> {

    protected final List<T> items;

    protected BaseLookupCollection(List<T> items) {
        this.items = items;
    }

    /**
     * Filters items using a specification.
     * <p>
     * This is a protected helper method intended for use within subclass public methods.
     * Consider using this to build well-named, domain-specific public methods.
     *
     * @param spec the specification to filter by
     * @return list of entities matching the specification
     */
    protected List<T> filter(LookupSpecification<T> spec) {
        return items.stream()
                .filter(spec.toPredicate())
                .toList();
    }

    /**
     * Returns all lookup items as entities.
     *
     * @return immutable list of all lookup entities
     */
    public List<T> getAll() {
        return items;
    }

    /**
     * Returns items as a stream for advanced filtering and mapping operations.
     * <p>
     * Use this when you need to chain multiple stream operations beyond simple filtering.
     *
     * @return stream of lookup entities
     */
    public Stream<T> stream() {
        return items.stream();
    }

    /**
     * Transforms all items to DTO format for controller responses.
     * <p>
     * This method is typically called by service layer methods that are exposed to controllers.
     *
     * @return list of DTOs containing only id and name
     */
    public List<LookupResponseDto> toDtoList() {
        return items.stream()
                .map(LookupResponseDto::fromEntity)
                .toList();
    }

    /**
     * Finds all items matching a specification.
     * <p>
     * Public version of {@code filter()} for external specification-based filtering.
     *
     * @param spec the specification to filter by
     * @return list of entities matching the specification
     */
    public List<T> findBy(LookupSpecification<T> spec) {
        return items.stream()
                .filter(spec.toPredicate())
                .toList();
    }

    /**
     * Finds the first item matching a specification.
     *
     * @param spec the specification to match
     * @return first matching entity, or {@code null} if no match found
     */
    public T findFirstBy(LookupSpecification<T> spec) {
        return items.stream()
                .filter(spec.toPredicate())
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds a lookup entity by its ID.
     *
     * @param id the lookup ID to search for
     * @return the matching entity, or {@code null} if not found
     */
    public T findById(Integer id) {
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}
