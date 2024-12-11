package com.sonnguyen.iamservice2.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class WorkbookValidationException extends RuntimeException{
    List<String> violations;
    public WorkbookValidationException(String message, List<String> violations) {
        super(message);
        this.violations=violations;
    }

    public static  WorkbookValidationException create(int rowIndex,int colIndex, List<String> violations) {
        return new WorkbookValidationException(String.format("Failure Validation in {row:%s, col::%s} ",rowIndex,colIndex),violations);
    }

}
