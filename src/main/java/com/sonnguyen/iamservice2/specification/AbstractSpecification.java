package com.sonnguyen.iamservice2.specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.sql.Restriction;

import java.sql.Timestamp;


public abstract class AbstractSpecification<T> implements GeneralSpecification<T> {
    private final DynamicSearch criteria;

    protected AbstractSpecification(DynamicSearch criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation()== DynamicSearch.Operator.GTE) {
            return builder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation()==DynamicSearch.Operator.LTE) {
            return builder.lessThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation()==DynamicSearch.Operator.LIKE) {
            return builder.like(
                    builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase());
        }
        else if(criteria.getOperation()==DynamicSearch.Operator.LT){
            return builder.lessThan(
                    root.get(criteria.getKey()),criteria.getValue().toString());
        }else if(criteria.getOperation()==DynamicSearch.Operator.GT){
            return builder.greaterThan(
                    root.<String>get(criteria.getKey()),criteria.getValue().toString());
        }
        else if(criteria.getOperation()==DynamicSearch.Operator.EQUALS){
            return builder.equal(
                    root.<String>get(criteria.getKey()),criteria.getValue().toString());
        }else if(criteria.getOperation()==DynamicSearch.Operator.BEFORE){
            return builder.lessThan(
                    root.<Timestamp>get(criteria.getKey()),Timestamp.valueOf(criteria.getValue().toString()));
        } else if(criteria.getOperation()==DynamicSearch.Operator.AFTER){
            return builder.greaterThan(
                    root.<Timestamp>get(criteria.getKey()),Timestamp.valueOf(criteria.getValue().toString()));
        }
        return null;
    }
}
