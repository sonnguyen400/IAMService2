package com.sonnguyen.iamservice2.specification;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class GeneralSpecificationImp {
    @PersistenceContext
    EntityManager entityManager;
}
