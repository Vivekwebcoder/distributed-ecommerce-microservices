package com.ecommerce.user_service.res;

import com.ecommerce.user_service.enums.AddressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponseDto {

    private Long id;
    private String userId;
    private String fullName;
    private String phoneNumber;
    private String flatHouseNo;
    private String areaStreet;
    private String landmark;
    private String city;
    private String state;
    private String pincode;
    private AddressType addressType;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
