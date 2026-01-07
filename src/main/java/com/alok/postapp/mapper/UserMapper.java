package com.alok.postapp.mapper;

import com.alok.postapp.dto.user.UserResponse;
import com.alok.postapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserResponse userToUserResponse(User user);
}
