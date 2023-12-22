package com.bakuard.simpleCrud.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.util.Set;

public class ValidatorUtil {

    private final Validator validator;

    public ValidatorUtil(Validator validator) {
        this.validator = validator;
    }

    public <T> T assertValid(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);

        assertEmpty(violations);

        return entity;
    }


    private <T> void assertEmpty(Set<ConstraintViolation<T>> violations) {
        if(!violations.isEmpty()) {
            String reasons = violations.stream().
                    map(ConstraintViolation::getMessage).
                    reduce((a, b) -> String.join(", ", a, b)).
                    orElseThrow();
            throw new ConstraintViolationException("Validation fail: " + reasons, violations);
        }
    }
}
