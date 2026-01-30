package com.alok.postapp.dto.chat;

public record ChatRequest(
        String userId,
        String message
) {
}
