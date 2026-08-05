package com.ecommerce.user_service.req;

import com.ecommerce.user_service.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequestDto {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be valid 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Flat / House No is required")
    private String flatHouseNo;

    @NotBlank(message = "Area / Street is required")
    private String areaStreet;

    private String landmark;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be 6 numeric digits")
    private String pincode;

    @Builder.Default
    private AddressType addressType = AddressType.HOME;

    @Builder.Default
    private Boolean isDefault = false;
}
