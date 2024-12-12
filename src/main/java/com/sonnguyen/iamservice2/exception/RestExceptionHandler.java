package com.sonnguyen.iamservice2.exception;

import com.sonnguyen.iamservice2.utils.ResponseMessage;
import com.sonnguyen.iamservice2.utils.ResponseMessageStatus;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseMessage.builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getConstraintViolations().stream().map((ConstraintViolation::getMessage)).collect(Collectors.joining("\n")))
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleIllegalArgument(IllegalArgumentException e) {
        return ResponseMessage.builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleMethodArgumentException(MethodArgumentNotValidException e) {
        return ResponseMessage.builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(Arrays.stream(e.getDetailMessageArguments()))
                .build();
    }

    @ExceptionHandler(DuplicatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleDuplicatedException(DuplicatedException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler({InvalidArgumentException.class, MissingServletRequestPartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleInvalidArgumentException(Exception e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }


    @ExceptionHandler({JwtException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleJwtException(Exception e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseMessage handleInvalidArgumentException(BadCredentialsException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseMessage handleAuthenticationException(AuthenticationException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseMessage handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage methodValidationException(MethodValidationException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseMessage handleAuthorizationException(AuthorizationDeniedException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }
    @ExceptionHandler(WorkbookValidationException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseMessage handleAuthorizationException(WorkbookValidationException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .content(e.messages)
                .message(e.getMessage())
                .build();
    }
}
