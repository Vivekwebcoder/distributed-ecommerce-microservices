package com.ecommerce.auth_service.service;

import com.ecommerce.auth_service.dto.request.ForgotPasswordRequest;
import com.ecommerce.auth_service.dto.request.LoginRequest;
import com.ecommerce.auth_service.dto.request.RegisterRequest;
import com.ecommerce.auth_service.dto.request.ResetPasswordRequest;

import com.ecommerce.auth_service.dto.response.ForgotPasswordResponse;
import com.ecommerce.auth_service.dto.response.LoginResponse;
import com.ecommerce.auth_service.dto.response.RegisterResponse;
import com.ecommerce.auth_service.dto.response.ResetPasswordResponse;


public interface AuthService {

    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);
    ResetPasswordResponse resetPassword( ResetPasswordRequest request);

}
