package com.ecommerce.auth_service.service.impl;

import com.ecommerce.auth_service.dto.request.RegisterRequest;
import com.ecommerce.auth_service.dto.response.RegisterResponse;
import com.ecommerce.auth_service.entity.User;
import com.ecommerce.auth_service.enums.AccountStatus;
import com.ecommerce.auth_service.exception.EmailAlreadyExistsException;
import com.ecommerce.auth_service.exception.PhoneNumberAlreadyExistsException;
import com.ecommerce.auth_service.repository.UserRepository;
import com.ecommerce.auth_service.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ecommerce.auth_service.dto.request.LoginRequest;
import com.ecommerce.auth_service.dto.response.LoginResponse;
import com.ecommerce.auth_service.exception.UserNotFoundException;
import com.ecommerce.auth_service.exception.InvalidPasswordException;
import com.ecommerce.auth_service.security.JwtService;
import com.ecommerce.auth_service.dto.request.ForgotPasswordRequest;
import com.ecommerce.auth_service.dto.request.ResetPasswordRequest;
import com.ecommerce.auth_service.dto.response.ForgotPasswordResponse;
import com.ecommerce.auth_service.dto.response.ResetPasswordResponse;
import java.time.LocalDateTime;
import java.util.UUID;
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {

    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
}

    @Override
    public RegisterResponse register(RegisterRequest request) {

        // Check Email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        // Check Phone Number
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("Phone number already exists.");
        }

        // Create User Object
        User user = new User();

        user.setFirstName(request.getFirstName());

        user.setLastName(request.getLastName());

        user.setEmail(request.getEmail());

        user.setPhoneNumber(request.getPhoneNumber());

        // Encrypt Password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        user.setStatus(AccountStatus.ACTIVE);

        user.setEmailVerified(false);

      // Save User
User savedUser = userRepository.save(user);

// Response
RegisterResponse response = new RegisterResponse();

response.setMessage("Registration Successful");
response.setUserId(savedUser.getId());
response.setEmail(savedUser.getEmail());
response.setRole(savedUser.getRole().name());

// Debug
System.out.println("========== RESPONSE ==========");
System.out.println("Message : " + response.getMessage());
System.out.println("UserId  : " + response.getUserId());
System.out.println("Email   : " + response.getEmail());
System.out.println("Role    : " + response.getRole());
System.out.println("==============================");

return response;
    }
    @Override
public LoginResponse login(LoginRequest request) {

    // Check whether email exists
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new UserNotFoundException("User not found with this email."));

    // Verify Password
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new InvalidPasswordException("Invalid password.");
    }

    // Create Response
    String token = jwtService.generateToken(user);

LoginResponse response = new LoginResponse();

response.setMessage("Login Successful");
response.setUserId(user.getId());
response.setEmail(user.getEmail());
response.setRole(user.getRole().name());
response.setToken(token);

return response;
    
}
@Override
public ForgotPasswordResponse forgotPassword(
        ForgotPasswordRequest request) {

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new UserNotFoundException(
                            "User not found with this email."
                    )
            );

    // Generate unique reset token
    String resetToken = UUID.randomUUID().toString();

    // Token valid for 15 minutes
    LocalDateTime expiryTime =
            LocalDateTime.now().plusMinutes(15);

    // Save token
    user.setResetToken(resetToken);

    user.setResetTokenExpiry(expiryTime);

    userRepository.save(user);

    ForgotPasswordResponse response =
            new ForgotPasswordResponse();

    response.setMessage(
            "Password reset token generated successfully. " +
            "Token: " + resetToken
    );

    return response;
}
@Override
public ResetPasswordResponse resetPassword(
        ResetPasswordRequest request) {

    User user = userRepository
            .findByResetToken(request.getToken())
            .orElseThrow(() ->
                    new UserNotFoundException(
                            "Invalid or expired reset token."
                    )
            );

    // Check token expiry
    if (user.getResetTokenExpiry() == null ||
            user.getResetTokenExpiry()
                    .isBefore(LocalDateTime.now())) {

        throw new IllegalArgumentException(
                "Reset token has expired."
        );
    }

    // Encrypt new password
    String encodedPassword =
            passwordEncoder.encode(
                    request.getNewPassword()
            );

    user.setPassword(encodedPassword);

    // Clear reset token after successful reset
    user.setResetToken(null);

    user.setResetTokenExpiry(null);

    userRepository.save(user);

    ResetPasswordResponse response =
            new ResetPasswordResponse();

    response.setMessage(
            "Password reset successfully."
    );

    return response;
}
}
