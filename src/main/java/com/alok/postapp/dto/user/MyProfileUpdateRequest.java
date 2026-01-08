package com.alok.postapp.dto.user;

import com.alok.postapp.annotations.AllowedRoles;
import com.alok.postapp.enums.Role;
import org.hibernate.validator.constraints.Length;

public record MyProfileUpdateRequest(
        @Length(min = 3, max = 40) String name,
        @AllowedRoles({Role.VIEWER, Role.CREATOR}) Role role
) {
}
