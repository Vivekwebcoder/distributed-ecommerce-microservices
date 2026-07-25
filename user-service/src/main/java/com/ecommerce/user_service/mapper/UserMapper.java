package com.ecommerce.user_service.mapper;

import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.req.CreateUserRequest;
import com.ecommerce.user_service.req.UpdateUserRequest;
import com.ecommerce.user_service.res.CreateUserResponse;
import com.ecommerce.user_service.res.UpdateUserResponse;

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

    public UpdateUserResponse toUpdateUserResponse(User user) {

        return UpdateUserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
    public void updateUser(User user, UpdateUserRequest request) {

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());

    }
}
