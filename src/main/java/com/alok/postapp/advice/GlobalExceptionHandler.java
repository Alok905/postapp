package com.alok.postapp.advice;

import com.alok.postapp.advice.templates.ApiError;
import com.alok.postapp.advice.templates.ApiResponse;
import com.alok.postapp.data.ConstraintMessageMapper;
import com.alok.postapp.data.DbConstraints;
import com.alok.postapp.exception.ConflictException;
import com.alok.postapp.exception.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.AuthenticationException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponse<Void>> buildResponseEntity(ApiError error, HttpStatus status) {
        return new ResponseEntity<>(ApiResponse.error(null, status, error), status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiError error = ApiError.builder()
                .message(ex.getMessage())
                .build();
        return buildResponseEntity(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ApiError error = ApiError.builder()
                .message("Input validation failed")
                .subErrors(ex.getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList()
                )
                .build();
        return buildResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Throwable cause = ex.getCause();

        String errorMessage = "Constraint Violation";

        if(cause instanceof ConstraintViolationException cve) {
            String constraintName = cve.getConstraintName();

            if(constraintName != null && constraintName.endsWith(DbConstraints.USER_EMAIL)) {
                errorMessage = ConstraintMessageMapper.CONSTRAINT_MESSAGES.get(DbConstraints.USER_EMAIL);
            }
        }

        ApiError error = ApiError.builder()
                .message(errorMessage)
                .build();

        return buildResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<Void>> handleDisabledException(DisabledException ex) {
        ApiError error = ApiError.builder()
                .message(ex.getMessage())
                .build();

        return buildResponseEntity(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        ApiError error = ApiError.builder()
                .message(ex.getMessage())
                .build();

        return buildResponseEntity(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleJwtException(JwtException ex) {
        ApiError error = ApiError.builder()
                .message(ex.getMessage())
                .build();

        return buildResponseEntity(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflictException(ConflictException ex) {
        ApiError error = ApiError.builder()
                .message(ex.getMessage())
                .build();

        return buildResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleEnumErrors(Exception ex) {
        ApiError error = ApiError.builder()
                .message("Invalid enum value provided")
                .build();

        return buildResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        ApiError error = ApiError.builder()
                .message(ex.getMessage())
                .build();

        return buildResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
