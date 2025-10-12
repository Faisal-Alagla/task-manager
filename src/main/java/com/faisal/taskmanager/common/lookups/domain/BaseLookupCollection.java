package com.faisal.taskmanager.common.lookups.domain;

import com.faisal.taskmanager.common.lookups.LookupResponseDto;
import com.faisal.taskmanager.common.lookups.specifications.LookupSpecification;
import com.faisal.taskmanager.utils.Interfaces.BaseLookupResponseInterface;

import java.util.List;
import java.util.stream.Stream;

public abstract class BaseLookupCollection<T extends BaseLookupResponseInterface> {

    protected final List<T> items;

    protected BaseLookupCollection(List<T> items) {
        this.items = items;
    }

    /**
     * Get all items as entities (for business logic in services)
     */
    public List<T> getAll() {
        return items;
    }

    /**
     * Get all items as stream (for filtering/mapping)
     */
    public Stream<T> stream() {
        return items.stream();
    }

    /**
     * Map to DTOs for controller responses
     */
    public List<LookupResponseDto> toDtoList() {
        return items.stream()
                .map(LookupResponseDto::fromEntity)
                .toList();
    }

    /**
     * Filter using a single specification
     */
    public List<T> findBy(LookupSpecification<T> spec) {
        return items.stream()
                .filter(spec.toPredicate())
                .toList();
    }

    /**
     * Filter using specification and return as DTOs
     */
    public List<LookupResponseDto> findByAsDto(LookupSpecification<T> spec) {
        return findBy(spec).stream()
                .map(LookupResponseDto::fromEntity)
                .toList();
    }

    /**
     * Find first matching specification
     */
    public T findFirstBy(LookupSpecification<T> spec) {
        return items.stream()
                .filter(spec.toPredicate())
                .findFirst()
                .orElse(null);
    }

    /**
     * Check if any item matches specification
     */
    public boolean anyMatch(LookupSpecification<T> spec) {
        return items.stream().anyMatch(spec.toPredicate());
    }

    /**
     * Check if all items match specification
     */
    public boolean allMatch(LookupSpecification<T> spec) {
        return items.stream().allMatch(spec.toPredicate());
    }

    /**
     * Count items matching specification
     */
    public long countBy(LookupSpecification<T> spec) {
        return items.stream()
                .filter(spec.toPredicate())
                .count();
    }

    /**
     * Override in subclasses to provide default filtering behavior
     */
    protected LookupSpecification<T> getDefaultSpecification() {
        return () -> item -> true; // No filtering by default
    }

    /**
     * Get filtered items (applies default filter defined in subclass)
     */
    public List<T> getFiltered() {
        return findBy(getDefaultSpecification());
    }

    /**
     * Get filtered items as DTOs
     */
    public List<LookupResponseDto> getFilteredDtos() {
        return findByAsDto(getDefaultSpecification());
    }

    /**
     * Find by ID - returns entity for business logic
     */
    public T findById(Integer id) {
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Find by name - returns entity for business logic
     */
    public T findByName(String name) {
        return items.stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get count
     */
    public int count() {
        return items.size();
    }

    /**
     * Check if contains item by ID
     */
    public boolean containsId(Integer id) {
        return items.stream().anyMatch(item -> item.getId().equals(id));
    }

    /**
     * Check if contains item by name
     */
    public boolean containsName(String name) {
        return items.stream().anyMatch(item -> item.getName().equalsIgnoreCase(name));
    }
}