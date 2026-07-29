package ecart.ecommerce.controller;

import ecart.ecommerce.dto.request.ForgotPasswordRequest;
import ecart.ecommerce.dto.request.LoginRequest;
import ecart.ecommerce.dto.request.RegisterRequest;
import ecart.ecommerce.dto.request.ResetPasswordRequest;

import ecart.ecommerce.dto.response.ForgotPasswordResponse;
import ecart.ecommerce.dto.response.LoginResponse;
import ecart.ecommerce.dto.response.RegisterResponse;
import ecart.ecommerce.dto.response.ResetPasswordResponse;

import ecart.ecommerce.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public AuthController(AuthService authService) {

        this.authService = authService;
    }


    // =========================================================
    // REGISTER
    // =========================================================

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid
            @RequestBody
            RegisterRequest request
    ) {

        RegisterResponse response =
                authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // LOGIN
    // =========================================================

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid
            @RequestBody
            LoginRequest request
    ) {

        LoginResponse response =
                authService.login(request);

        return ResponseEntity
                .ok(response);
    }


    // =========================================================
    // FORGOT PASSWORD
    // =========================================================

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
            @Valid
            @RequestBody
            ForgotPasswordRequest request
    ) {

        ForgotPasswordResponse response =
                authService.forgotPassword(request);

        return ResponseEntity
                .ok(response);
    }


    // =========================================================
    // RESET PASSWORD
    // =========================================================

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(
            @Valid
            @RequestBody
            ResetPasswordRequest request
    ) {

        ResetPasswordResponse response =
                authService.resetPassword(request);

        return ResponseEntity
                .ok(response);
    }
}