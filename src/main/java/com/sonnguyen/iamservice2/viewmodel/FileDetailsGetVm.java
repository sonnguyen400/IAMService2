package com.sonnguyen.iamservice2.viewmodel;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FileDetailsGetVm(
    String name,
    String path,
    String link,
    String owner,
    Integer version,
    Long size,
    String contentType,
    String accessType,
    LocalDateTime createdDate,
    LocalDateTime lastModifiedDate
){

}
