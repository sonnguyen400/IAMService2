package com.sonnguyen.iamservice2.validator.excel_validator;

public interface IValidator <T>{
    boolean validate(T value);
}
