package com.jobsite_management_service.abstraction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

// strategy interface for a dynamic query builder
@FunctionalInterface
public interface ReportSpecification<T> {
    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);

    // Dynamic AND composition
    default ReportSpecification<T> and(ReportSpecification<T> other) {
        return (root, query, cb) -> cb.and(this.toPredicate(root, query, cb), other.toPredicate(root, query, cb));
    }
}
