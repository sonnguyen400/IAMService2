package com.sonnguyen.iamservice2.controller;

import com.sonnguyen.iamservice2.service.PublicStorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public/file")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class PublicStorageController {
    PublicStorageService storageService;
    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam Map<String,String[]> params){
        return storageService.findAll(params);
    }
    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id){
        return storageService.findById(id);
    }
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadFileById(@PathVariable(name = "id") Long fileId){
        return storageService.downloadFileById(fileId);
    }
    @GetMapping("/{id}/download/thumbnail")
    ResponseEntity<byte[]> downloadThumbnail(@PathVariable Long id,@RequestParam Map<String,String[]> thumbnailParamsVm){
        return storageService.downloadThumbnail(id, thumbnailParamsVm);
    }
    @PostMapping("/{id}/delete")
    ResponseEntity<?> deleteById(@PathVariable Long id){
        return storageService.deleteById(id);
    }
    @PostMapping("/upload")
    ResponseEntity<?> uploadAllFile(@RequestPart(name = "file") List<MultipartFile> files, Authentication authentication){
        String owner="anonymous";
        if(authentication!=null) owner=(String)authentication.getPrincipal();
        return storageService.uploadAllFile(files,owner);
    }
}
