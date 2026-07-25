package com.ecommerce.user_service.res;

import com.ecommerce.user_service.enums.Role;
import com.ecommerce.user_service.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateUserResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Role role;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
