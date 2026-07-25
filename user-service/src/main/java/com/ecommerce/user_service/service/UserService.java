package com.ecommerce.user_service.service;

import com.ecommerce.user_service.req.CreateUserRequest;
import com.ecommerce.user_service.req.UpdateUserRequest;
import com.ecommerce.user_service.res.CreateUserResponse;
import com.ecommerce.user_service.res.UpdateUserResponse;

import java.util.List;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest request);
    List<CreateUserResponse> getAllUsers();
    UpdateUserResponse updateUser(Long id, UpdateUserRequest request);
}
