package com.sonnguyen.iamservice2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
@Component
public class PermissionEvaluatorConfig implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if(authentication==null||targetDomainObject==null||!(permission instanceof String)) return false;
        String targetType=targetDomainObject.toString().toUpperCase();
        return containPermission(authentication,targetType, permission.toString().toUpperCase());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if(authentication==null||targetType==null||!(permission instanceof String)) return false;
        return containPermission(authentication,targetType, permission.toString().toUpperCase());
    }
    private boolean containPermission(Authentication authentication, String targetType, String permission) {
        for(GrantedAuthority authority : authentication.getAuthorities()) {
            if(authority.getAuthority().startsWith(targetType)&&authority.getAuthority().contains(permission)){
                return true;
            }
        }
        return false;
    }
}