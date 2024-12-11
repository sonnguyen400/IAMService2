package com.sonnguyen.iamservice2.validator.excel_validator;

public class NonBlank implements IValidator<String>{
    @Override
    public boolean validate(String value) {
        return value!=null&&!value.isBlank();
    }
}
