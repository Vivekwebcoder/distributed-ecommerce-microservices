package com.ecommerce.user_service.service;

import com.ecommerce.user_service.req.AddressRequestDto;
import com.ecommerce.user_service.res.AddressResponseDto;

import java.util.List;

public interface AddressService {

    AddressResponseDto addAddress(String userId, AddressRequestDto request);

    List<AddressResponseDto> getAddressesByUserId(String userId);

    AddressResponseDto getAddressById(String userId, Long id);

    AddressResponseDto updateAddress(String userId, Long id, AddressRequestDto request);

    void deleteAddress(String userId, Long id);

    void setDefaultAddress(String userId, Long id);
}
