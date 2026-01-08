package com.alok.postapp.mapper;

import com.alok.postapp.dto.auth.SignupRequest;
import com.alok.postapp.dto.user.UserProfileResponse;
import com.alok.postapp.dto.user.MyProfileUpdateRequest;
import com.alok.postapp.dto.user.UserProfileUpdateRequest;
import com.alok.postapp.dto.user.UserResponse;
import com.alok.postapp.entity.User;
import com.alok.postapp.enums.Permission;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = "spring"

)
public interface UserMapper {
    User signUpRequstToUser(SignupRequest signupRequest);

    UserResponse userToUserResponse(User user);

    List<UserResponse> usersToUserResponses(List<User> user);

    UserProfileResponse userToUserProfileResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void myProfileUpdateRequestToUser(MyProfileUpdateRequest myProfileUpdateRequest, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void userProfileUpdateRequestToUser(UserProfileUpdateRequest myProfileUpdateRequest, @MappingTarget User user);
}
