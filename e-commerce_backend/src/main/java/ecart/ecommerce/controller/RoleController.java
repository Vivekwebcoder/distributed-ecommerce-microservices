package ecart.ecommerce.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    // ADMIN only
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminApi() {
        return "Welcome Admin! You have ADMIN access.";
    }

    // SELLER only
    @GetMapping("/seller")
    @PreAuthorize("hasRole('SELLER')")
    public String sellerApi() {
        return "Welcome Seller! You have SELLER access.";
    }

    // CUSTOMER only
    @GetMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String customerApi() {
        return "Welcome Customer! You have CUSTOMER access.";
    }

    // ADMIN or SELLER
    @GetMapping("/admin-seller")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public String adminSellerApi() {
        return "Welcome! You have ADMIN or SELLER access.";
    }
}