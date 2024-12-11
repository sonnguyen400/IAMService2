package com.sonnguyen.iamservice2.validator.excel_validator;

public class ValidatorContext {
    private IValidator validator;

    public ValidatorContext(IValidator validator) {
        this.validator = validator;
    }

    public boolean validate(String field) {
        return this.validator.validate(field);
    }
}
