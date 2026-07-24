package com.ecommerce.user_service.mapper;

import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.req.CreateUserRequest;
import com.ecommerce.user_service.res.CreateUserResponse;

public class UserMapper {
    public static User toEntity(CreateUserRequest request) {

        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }

    public static CreateUserResponse toResponse(User user) {

        return CreateUserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
