package com.ecommerce.user_service.service;

import com.ecommerce.user_service.req.AddressRequestDto;
import com.ecommerce.user_service.res.AddressResponseDto;

import java.util.List;

public interface AddressService {

    AddressResponseDto addAddress(Long userId, AddressRequestDto request);

    List<AddressResponseDto> getAddressesByUserId(Long userId);

    AddressResponseDto getAddressById(Long userId, Long id);

    AddressResponseDto updateAddress(Long userId, Long id, AddressRequestDto request);

    void deleteAddress(Long userId, Long id);

    void setDefaultAddress(Long userId, Long id);
}
