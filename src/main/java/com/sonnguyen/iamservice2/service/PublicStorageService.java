package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.viewmodel.FileUploadedResponseVm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@FeignClient(name = "publicStorageService",url = "http://localhost:8086/api/v1/public/file")
public interface PublicStorageService {
    @GetMapping
    ResponseEntity<?> findAll(@RequestParam Map<String,String[]> requestParams);
    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id);
    @GetMapping("/{id}/download")
    ResponseEntity<byte[]> downloadFileById(@PathVariable(name = "id") Long fileId);
    @GetMapping("/{id}/download/thumbnail")
    ResponseEntity<byte[]> downloadThumbnail(@PathVariable Long id,@RequestParam Map<String,String[]> thumbnailParamsVm);
    @PostMapping("/{id}/delete")
    ResponseEntity<?> deleteById(@PathVariable Long id);
    @PostMapping(value = "/upload",consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<List<FileUploadedResponseVm>> uploadAllFile(@RequestPart(name = "file") List<MultipartFile> files, @RequestPart String owner);
}
