package com.sonnguyen.iamservice2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
// enable method security
@EnableMethodSecurity
public class MethodSecConfig {

    @Bean
    static MethodSecurityExpressionHandler expressionHandler(
            PermissionEvaluatorConfig customPermissionEvaluator) {

        // create a new DefaultMethodSecurityExpressionHandler 
        // that will utilize CustomPermissionEvaluator
        DefaultMethodSecurityExpressionHandler handler =
                new DefaultMethodSecurityExpressionHandler();
        // add the PermissionEvaluator
        handler.setPermissionEvaluator(customPermissionEvaluator);
        return handler;
    }
}
