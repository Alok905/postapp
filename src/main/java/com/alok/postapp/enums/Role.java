package com.alok.postapp.enums;

import lombok.Getter;

import java.util.Set;

import static com.alok.postapp.enums.Permission.*;

@Getter  // for "getPermissions()" method
public enum Role {
    CREATOR(Set.of(
            POST_CREATE,
            POST_VIEW,
            POST_EDIT,
            POST_DELETE
    )),
    VIEWER(Set.of(
                POST_VIEW
    )),
    ADMIN(Set.of(Permission.values()));

    final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

}
