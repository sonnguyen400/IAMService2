package com.sonnguyen.iamservice2.controller;

import com.sonnguyen.iamservice2.service.PermissionService;
import com.sonnguyen.iamservice2.viewmodel.PermissionGetVm;
import com.sonnguyen.iamservice2.viewmodel.PermissionPostVm;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permission")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level= AccessLevel.PRIVATE)
public class PermissionController {
    PermissionService permissionService;
    @GetMapping("/ids")
    public List<PermissionGetVm> findAllById(@RequestParam List<Long> id){
        return permissionService.findAllByIdIn(id);
    }
    @GetMapping
    public Page<PermissionGetVm> findAll(
            @RequestParam(name = "page",defaultValue = "0",required = false) Integer page,
            @RequestParam(name="size", defaultValue = "10",required = false) Integer size
    ){
        return permissionService.findAll(PageRequest.of(page,size));
    }
    @PostMapping
    public List<PermissionGetVm> createPermissions(@RequestBody @Valid List<PermissionPostVm> permissionPostVms){
        return permissionService.createPermissions(permissionPostVms);
    }
    @PostMapping("/{id}/update")
    public PermissionGetVm updatePermissionById(
            @PathVariable Long id,
            @RequestBody @Valid PermissionPostVm permissionPostVm){
        return permissionService.updatePermissionById(id,permissionPostVm);
    }
    @PostMapping("/{id}/delete")
    public void deletePermissionById(
            @PathVariable Long id){
         permissionService.deleteById(id);
    }
}
