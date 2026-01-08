package com.alok.postapp.service.impl;

import com.alok.postapp.dto.user.*;
import com.alok.postapp.entity.User;
import com.alok.postapp.enums.Permission;
import com.alok.postapp.exception.ResourceNotFoundException;
import com.alok.postapp.mapper.UserMapper;
import com.alok.postapp.repository.UserRepository;
import com.alok.postapp.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "email="+username));
    }

    @Override
    public User getUserById(Long userId, boolean throwException) {
        if(throwException)
            return userRepository
                    .findById(userId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("User", "id="+userId));
        return userRepository
                .findById(userId)
                .orElse(null);
    }

    @Override
    public User getUserByEmail(String email, boolean throwException) {
        if(throwException)
            return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "email="+email));
        return userRepository
                    .findByEmail(email)
                    .orElse(null);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserProfileResponse getMyProfile() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userMapper.userToUserProfileResponse(user);
    }

    @Override
    @Transactional
    public UserProfileResponse updateMyProfile(MyProfileUpdateRequest myProfileUpdateRequest) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = getUserById(authenticatedUser.getId(), true);

        userMapper.myProfileUpdateRequestToUser(myProfileUpdateRequest, user);

        User updatedUser = userRepository.save(user);

        return userMapper.userToUserProfileResponse(updatedUser);
    }

    @Override
    @Transactional
    public String deactivateMe() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        user.setDeactivatedAt(Instant.now());
        userRepository.save(user);

        return "User has been deactivated";
    }

    @Override
    public UsersListResponse getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = userMapper.usersToUserResponses(users);

        return new UsersListResponse(
                userResponses,
                userResponses.size()
        );
    }

    @Override
    public UserResponse getSingleUserById(Long userId) {
        User user = getUserById(userId, true);
        return userMapper.userToUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateSingleUserById(Long userId, UserProfileUpdateRequest userProfileUpdateRequest) {
        User user = getUserById(userId, true);

        userMapper.userProfileUpdateRequestToUser(userProfileUpdateRequest, user);

        ///  mapstruct by default doesn't merge the collections
        Set<Permission> permissionSet = new HashSet<>(user.getStoredPermissions());
        if(userProfileUpdateRequest.permissions() != null)
            permissionSet.addAll(userProfileUpdateRequest.permissions());

        user.setPermissions(permissionSet);

        User updatedUser = userRepository.save(user);

        return userMapper.userToUserResponse(updatedUser);
    }

}
