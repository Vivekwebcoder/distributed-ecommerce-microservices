package com.ecommerce.user_service.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal) {
            if (principal.getUserId() != null) {
                return principal.getUserId();
            }
        }
        throw new AccessDeniedException("User is not authenticated or user ID could not be identified");
    }
}
