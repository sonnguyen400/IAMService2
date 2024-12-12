package com.sonnguyen.iamservice2.viewmodel;


public record FileDataListGetVm(
        Long id,
        String name,
        String path,
        String link,
        Integer version,
        Long size,
        String contentType,
        String accessType,
        String createdDate,
        String lastModifiedDate
) {
}
