package com.ecommerce.user_service.service.impl;

import com.ecommerce.user_service.entity.Address;
import com.ecommerce.user_service.exception.DuplicateResourceException;
import com.ecommerce.user_service.exception.ResourceNotFoundException;
import com.ecommerce.user_service.repository.AddressRepository;
import com.ecommerce.user_service.req.AddressRequestDto;
import com.ecommerce.user_service.res.AddressResponseDto;
import com.ecommerce.user_service.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public AddressResponseDto addAddress(String userId, AddressRequestDto request) {
        if (addressRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number " + request.getPhoneNumber() + " is already in use by another address");
        }

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.resetDefaultAddressForUser(userId);
        }

        Address address = Address.builder()
                .userId(userId)
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .flatHouseNo(request.getFlatHouseNo())
                .areaStreet(request.getAreaStreet())
                .landmark(request.getLandmark())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .addressType(request.getAddressType())
                .isDefault(request.getIsDefault() != null ? request.getIsDefault() : false)
                .build();

        Address savedAddress = addressRepository.save(address);
        return mapToResponseDto(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponseDto> getAddressesByUserId(String userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);
        return addresses.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponseDto getAddressById(String userId, Long id) {
        Address address = addressRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address with ID " + id + " not found for this user"));
        return mapToResponseDto(address);
    }

    @Override
    public AddressResponseDto updateAddress(String userId, Long id, AddressRequestDto request) {
        Address existingAddress = addressRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address with ID " + id + " not found for this user"));

        if (addressRepository.existsByPhoneNumberAndIdNot(request.getPhoneNumber(), id)) {
            throw new DuplicateResourceException("Phone number " + request.getPhoneNumber() + " is already in use by another address");
        }

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.resetDefaultAddressForUser(userId);
        }

        existingAddress.setFullName(request.getFullName());
        existingAddress.setPhoneNumber(request.getPhoneNumber());
        existingAddress.setFlatHouseNo(request.getFlatHouseNo());
        existingAddress.setAreaStreet(request.getAreaStreet());
        existingAddress.setLandmark(request.getLandmark());
        existingAddress.setCity(request.getCity());
        existingAddress.setState(request.getState());
        existingAddress.setPincode(request.getPincode());
        existingAddress.setAddressType(request.getAddressType());
        existingAddress.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);

        Address updatedAddress = addressRepository.save(existingAddress);
        return mapToResponseDto(updatedAddress);
    }

    @Override
    public void deleteAddress(String userId, Long id) {
        Address address = addressRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address with ID " + id + " not found for this user"));
        addressRepository.delete(address);
    }

    @Override
    public void setDefaultAddress(String userId, Long id) {
        Address address = addressRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address with ID " + id + " not found for this user"));

        addressRepository.resetDefaultAddressForUser(userId);
        address.setIsDefault(true);
        addressRepository.save(address);
    }

    private AddressResponseDto mapToResponseDto(Address address) {
        return AddressResponseDto.builder()
                .id(address.getId())
                .userId(address.getUserId())
                .fullName(address.getFullName())
                .phoneNumber(address.getPhoneNumber())
                .flatHouseNo(address.getFlatHouseNo())
                .areaStreet(address.getAreaStreet())
                .landmark(address.getLandmark())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .addressType(address.getAddressType())
                .isDefault(address.getIsDefault())
                .createdAt(address.getCreatedAt())
                .updatedAt(address.getUpdatedAt())
                .build();
    }
}
