package com.sonnguyen.iamservice2.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.sql.Timestamp;


public abstract class AbstractSpecification<T> implements GeneralSpecification<T> {
    private final DynamicSearch criteria;

    protected AbstractSpecification(DynamicSearch criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return switch (criteria.getOperation()){
            case GTE ->builder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
            case LTE ->builder.lessThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE -> builder.like(
                    builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase());
            case LT -> builder.lessThan(
                    root.get(criteria.getKey()),criteria.getValue().toString());
            case GT -> builder.greaterThan(
                    root.<String>get(criteria.getKey()),criteria.getValue().toString());
            case EQUAL -> builder.equal(
                    root.<String>get(criteria.getKey()),criteria.getValue().toString());
            case BEFORE -> builder.lessThan(
                    root.<Timestamp>get(criteria.getKey()),Timestamp.valueOf(criteria.getValue().toString()));
            case AFTER ->  builder.greaterThan(
                    root.<Timestamp>get(criteria.getKey()),Timestamp.valueOf(criteria.getValue().toString()));
            default -> builder.like(
                    builder.lower(root.get(criteria.getKey())), String.format("%%%s%%",criteria.getValue().toString().toLowerCase()));
        };
    }
}
