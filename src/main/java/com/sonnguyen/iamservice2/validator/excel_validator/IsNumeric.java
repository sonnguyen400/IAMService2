package com.sonnguyen.iamservice2.validator.excel_validator;

public class IsNumeric<T> implements IValidator<T>{
    @Override
    public boolean validate(T value) {
        return value instanceof Number;
    }
}
