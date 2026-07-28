package com.ecommerce.user_service.service.impl;

import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.enums.Role;
import com.ecommerce.user_service.enums.Status;
import com.ecommerce.user_service.exception.EmailAlreadyExistsException;
import com.ecommerce.user_service.exception.PhoneNumberAlreadyExistsException;
import com.ecommerce.user_service.exception.UserNotFoundException;
import com.ecommerce.user_service.mapper.UserMapper;
import com.ecommerce.user_service.repository.UserRepository;
import com.ecommerce.user_service.req.CreateUserRequest;
import com.ecommerce.user_service.req.UpdateUserRequest;
import com.ecommerce.user_service.res.CreateUserResponse;
import com.ecommerce.user_service.res.UpdateUserResponse;
import com.ecommerce.user_service.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = new UserMapper();

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

    @Override
    public List<CreateUserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UpdateUserResponse updateUser(Long id, UpdateUserRequest request) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id : " + id));

            userMapper.updateUser(user, request);
            user.setUpdatedAt(LocalDateTime.now());
            User updatedUser = userRepository.save(user);

            return userMapper.toUpdateUserResponse(updatedUser);

    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id : " + id));
        userRepository.delete(user);
    }
}
