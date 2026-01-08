package com.alok.postapp.dto.auth;

///  if we want to send some more details then it can be useful; that's why I didn't mark the return type as String
public record UserActivateSuccessResponse(
        String message
) {
}
