package com.alok.postapp.mapper;

import com.alok.postapp.dto.auth.SignupRequest;
import com.alok.postapp.dto.user.UserResponse;
import com.alok.postapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User signUpRequstToUser(SignupRequest signupRequest);

    UserResponse userToUserResponse(User user);
}
