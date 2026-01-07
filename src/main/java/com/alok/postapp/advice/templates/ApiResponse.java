package com.alok.postapp.advice.templates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
public class ApiResponse<T> {
    private final T data;
    private final Instant timestamp;
    private final HttpStatus status;
    private final ApiError error;

    private ApiResponse(T data, HttpStatus status, ApiError error) {
        this.data = data;
        this.status = status;
        this.error = error;
        this.timestamp = Instant.now();
    }

    // factory methods
    public static <T> ApiResponse<T> success(T data, HttpStatus status) {
        return new ApiResponse<>(data, status, null);
    }
    public static <T> ApiResponse<T> error(T data, HttpStatus status, ApiError error) {
        return new ApiResponse<>(data, status, error);
    }
}
