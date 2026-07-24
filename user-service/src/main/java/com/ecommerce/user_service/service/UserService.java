package com.ecommerce.user_service.service;

import com.ecommerce.user_service.req.CreateUserRequest;
import com.ecommerce.user_service.res.CreateUserResponse;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest request);
}
