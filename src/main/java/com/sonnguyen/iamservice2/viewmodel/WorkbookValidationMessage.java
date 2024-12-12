package com.sonnguyen.iamservice2.viewmodel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record WorkbookValidationMessage(Integer row, Integer column, List<String> message) {
}
