package com.ecommerce.user_service.res;

import com.ecommerce.user_service.enums.Role;
import com.ecommerce.user_service.enums.Status;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Role role;

    private Status status;

    private LocalDateTime createdAt;
}
