//package com.sonnguyen.iamservice2.config;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Order(1)
//@Component
//@Slf4j
//public class LoggingConfig {
//    @Autowired
//    HttpServletRequest request;
//
//    @Before("within(com.sonnguyen.iamservice2.controller.*)")
//    public void endpointBefore(JoinPoint p) {
//        log.info(p.getTarget().getClass().getSimpleName() + " " + p.getSignature().getName() + " START");
//        Object[] signatureArgs = p.getArgs();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//
//        try {
//            if (signatureArgs[0] != null) {
//                log.info("Request object:{}" , mapper.writeValueAsString(signatureArgs[0]));
//            }
//        } catch (JsonProcessingException ignored) {
//        }
//    }
//
//    @AfterReturning(value = "within(com.sonnguyen.iamservice2.controller.*)", returning = "returnValue")
//    public void endpointAfterReturning(JoinPoint p, Object returnValue) {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        try {
//            log.info("Response object: {}" , mapper.writeValueAsString(returnValue));
//        } catch (JsonProcessingException e) {
//            System.out.println(e.getMessage());
//        }
//        log.info(p.getTarget().getClass().getSimpleName() + " " + p.getSignature().getName() + " END");
//    }
//
//
//    @AfterThrowing(pointcut = ("within(com.sonnguyen.iamservice2.controller"), throwing = "e")
//    public void endpointAfterThrowing(JoinPoint p, Exception e) throws Exception {
//        System.out.println("Aspect");
//        e.printStackTrace();
//        log.error(p.getTarget().getClass().getSimpleName() + " " + p.getSignature().getName() + " " + e.getMessage());
//    }
//}