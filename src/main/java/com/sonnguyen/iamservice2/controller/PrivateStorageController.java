package com.sonnguyen.iamservice2.controller;

import com.sonnguyen.iamservice2.service.PrivateStorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/private/file")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class PrivateStorageController {
    PrivateStorageService storageService;
    @GetMapping
    @PreAuthorize("hasPermission('FILE','READ')")
    ResponseEntity<?>findAll(HttpServletRequest httpServletRequest){
        return storageService.findAll(httpServletRequest.getParameterMap());
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('FILE','READ')")
    ResponseEntity<?> findById(@PathVariable Long id){
        return storageService.findById(id);
    }
    @GetMapping("/{id}/download")
    @PreAuthorize("hasPermission('FILE','READ')")
    public ResponseEntity<byte[]> downloadFileById(@PathVariable(name = "id") Long fileId){
        return storageService.downloadFileById(fileId);
    }
    @GetMapping("/{id}/download/thumbnail")
    @PreAuthorize("hasPermission('FILE','READ')")
    ResponseEntity<byte[]> downloadThumbnail(@PathVariable Long id,@RequestParam Map<String,String[]> thumbnailParamsVm){
        return storageService.downloadThumbnail(id, thumbnailParamsVm);
    }
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasPermission('FILE','DELETE')")
    ResponseEntity<?> deleteById(@PathVariable Long id){
        return storageService.deleteById(id);
    }
    @PostMapping("/upload")
    @PreAuthorize("hasPermission('FILE','CREATE')")
    ResponseEntity<?> uploadAllFile(@RequestPart(name = "file") List<MultipartFile> files, Authentication authentication){
        String owner="anonymous";
        if(authentication!=null) owner=(String)authentication.getPrincipal();
        return storageService.uploadAllFile(files, owner);
    }
}
