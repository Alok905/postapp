package com.alok.postapp.service;

import com.alok.postapp.dto.user.*;
import com.alok.postapp.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User getUserById(Long userId, boolean throwException);

    User getUserByEmail(String email, boolean throwException);

    UserProfileResponse getMyProfile();

    UserProfileResponse updateMyProfile(MyProfileUpdateRequest myProfileUpdateRequest);

    String deactivateMe();

    UsersListResponse getAllUsers();

    UserResponse getSingleUserById(Long userId);

    UserResponse updateSingleUserById(Long useId, UserProfileUpdateRequest userProfileUpdateRequest);
}
