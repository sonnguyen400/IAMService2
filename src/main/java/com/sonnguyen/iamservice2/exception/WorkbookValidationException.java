package com.sonnguyen.iamservice2.exception;

import com.sonnguyen.iamservice2.viewmodel.WorkbookValidationMessage;
import lombok.Getter;

import java.util.List;

@Getter
public class WorkbookValidationException extends RuntimeException{
    List<WorkbookValidationMessage> messages;
    public WorkbookValidationException(List<WorkbookValidationMessage> messages) {
        super();
        this.messages=messages;
    }
}
