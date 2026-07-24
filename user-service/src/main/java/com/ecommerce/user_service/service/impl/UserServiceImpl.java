package com.ecommerce.user_service.service.impl;

import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.enums.Role;
import com.ecommerce.user_service.enums.Status;
import com.ecommerce.user_service.exception.EmailAlreadyExistsException;
import com.ecommerce.user_service.exception.PhoneNumberAlreadyExistsException;
import com.ecommerce.user_service.mapper.UserMapper;
import com.ecommerce.user_service.repository.UserRepository;
import com.ecommerce.user_service.req.CreateUserRequest;
import com.ecommerce.user_service.res.CreateUserResponse;
import com.ecommerce.user_service.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        // Check if phone number already exists
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("Phone number already exists.");
        }

        // Convert Request DTO to Entity
        User user = UserMapper.toEntity(request);

        // Set default values
        user.setRole(Role.CUSTOMER);
        user.setStatus(Status.ACTIVE);

        // Save user
        User savedUser = userRepository.save(user);

        // Convert Entity to Response DTO
        return UserMapper.toResponse(savedUser);
    }
}
