package com.alok.postapp.controller;

import com.alok.postapp.dto.user.*;
import com.alok.postapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /// this is used by the user itself to get his profile
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile() {
        UserProfileResponse userProfileResponse = userService.getMyProfile();
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMyProfile(@Valid @RequestBody MyProfileUpdateRequest myProfileUpdateRequest) {
        UserProfileResponse userProfileResponse = userService.updateMyProfile(myProfileUpdateRequest);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    @PostMapping("/me/deactivate")
    public ResponseEntity<String> deactivateMe() {
        String response = userService.deactivateMe();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/users")
    @PreAuthorize("@security.hasUserViewPermission()") // it'll get the authorities from user and check if any of those matches with "ROLE_ADMIN"
    public ResponseEntity<UsersListResponse> getAllUsers() {
        UsersListResponse usersListResponse = userService.getAllUsers();
        return new ResponseEntity<>(usersListResponse, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
//    @PreAuthorize("hasRole('ADMIN')") // it'll get the authorities from user and check if any of those matches with "ROLE_ADMIN"
    @PreAuthorize("@security.hasUserViewPermission()") // it'll get the authorities from user and check if any of those matches with "ROLE_ADMIN"
    public ResponseEntity<UserResponse> getSingleUserById(@PathVariable("userId") Long userId) {
        UserResponse userResponse = userService.getSingleUserById(userId);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}")
    @PreAuthorize("@security.hasUserEditPermission()")
    public ResponseEntity<UserResponse> updateSingleUserById(
            @PathVariable("userId") Long userId,
            @Valid @RequestBody UserProfileUpdateRequest userProfileUpdateRequest
            ) {
        UserResponse userResponse = userService.updateSingleUserById(userId, userProfileUpdateRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
