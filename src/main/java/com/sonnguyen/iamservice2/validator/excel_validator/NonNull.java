package com.sonnguyen.iamservice2.validator.excel_validator;

public class NonNull<T> implements IValidator<T>{
    @Override
    public boolean validate(T value) {
        return value!=null;
    }
}
