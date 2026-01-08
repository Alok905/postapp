package com.alok.postapp.dto.user;

import java.util.List;

public record UsersListResponse(
        List<UserResponse> users,
        Integer total
) {
}
