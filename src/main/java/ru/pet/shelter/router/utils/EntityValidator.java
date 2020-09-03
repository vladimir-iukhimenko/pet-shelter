package ru.pet.shelter.router.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ServerWebInputException;

@Component
public class EntityValidator<T> {
    private T entity;
    private Validator validator;

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public void validate(T entity) {
        this.setEntity(entity);
        this.validateInternal();
    }

    private void validateInternal() {
        Errors errors = new BeanPropertyBindingResult(entity, entity.getClass().getSimpleName());
        validator.validate(entity, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}
