package com.alok.postapp.enums;

import lombok.Getter;

import java.util.Set;


@Getter  // for "getPermissions()" method
public enum Role {
    CREATOR(Set.of(
            Permission.POST_CREATE,
            Permission.POST_VIEW,
            Permission.POST_EDIT,
            Permission.POST_DELETE
    )),
    VIEWER(Set.of(
            Permission.POST_VIEW
    )),
    ADMIN(Set.of(Permission.values()));

    final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

}
