package com.sonnguyen.iamservice2.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage extends AbstractResponseMessage {
    public ResponseMessage() {
        super();
    }

    @Builder
    ResponseMessage(int status, Object message, Object content) {
        super(status, message, content);
    }
}
