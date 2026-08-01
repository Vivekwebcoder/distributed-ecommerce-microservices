package com.ecommerce.user_service.controller;

import com.ecommerce.user_service.req.AddressRequestDto;
import com.ecommerce.user_service.res.AddressResponseDto;
import com.ecommerce.user_service.res.ApiResponse;
import com.ecommerce.user_service.security.SecurityUtils;
import com.ecommerce.user_service.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponseDto>> addAddress(
            @Valid @RequestBody AddressRequestDto request) {
        String userId = SecurityUtils.getAuthenticatedUserId();
        AddressResponseDto response = addressService.addAddress(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Address added successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponseDto>>> getUserAddresses() {
        String userId = SecurityUtils.getAuthenticatedUserId();
        List<AddressResponseDto> list = addressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Addresses retrieved successfully", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponseDto>> getAddressById(
            @PathVariable Long id) {
        String userId = SecurityUtils.getAuthenticatedUserId();
        AddressResponseDto response = addressService.getAddressById(userId, id);
        return ResponseEntity.ok(ApiResponse.success("Address details fetched", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponseDto>> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequestDto request) {
        String userId = SecurityUtils.getAuthenticatedUserId();
        AddressResponseDto response = addressService.updateAddress(userId, id, request);
        return ResponseEntity.ok(ApiResponse.success("Address updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable Long id) {
        String userId = SecurityUtils.getAuthenticatedUserId();
        addressService.deleteAddress(userId, id);
        return ResponseEntity.ok(ApiResponse.success("Address deleted successfully", null));
    }

    @PutMapping("/{id}/default")
    public ResponseEntity<ApiResponse<Void>> setDefaultAddress(
            @PathVariable Long id) {
        String userId = SecurityUtils.getAuthenticatedUserId();
        addressService.setDefaultAddress(userId, id);
        return ResponseEntity.ok(ApiResponse.success("Set as default address successfully", null));
    }
}
