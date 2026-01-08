package com.alok.postapp.dto.auth;

import java.time.Instant;

public record UserActivateResponse(
        String message,
        String validity
) {
}
