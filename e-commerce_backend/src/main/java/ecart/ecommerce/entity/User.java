package ecart.ecommerce.entity;

import ecart.ecommerce.enums.AccountStatus;
import ecart.ecommerce.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "users")
public class User implements UserDetails {


    // =========================================================
    // PRIMARY KEY
    // =========================================================

    @Id
    @GeneratedValue
    private UUID id;


    // =========================================================
    // USER BASIC DETAILS
    // =========================================================

    @Column(
            name = "first_name",
            nullable = false
    )
    private String firstName;


    @Column(
            name = "last_name",
            nullable = false
    )
    private String lastName;


    // =========================================================
    // EMAIL
    // =========================================================

    @Column(
            nullable = false,
            unique = true
    )
    private String email;


    // =========================================================
    // PHONE NUMBER
    // =========================================================

    @Column(
            name = "phone_number",
            unique = true
    )
    private String phoneNumber;


    // =========================================================
    // PASSWORD
    // =========================================================

    @Column(
            nullable = false
    )
    private String password;


    // =========================================================
    // ROLE
    // CUSTOMER / SELLER / ADMIN
    // =========================================================

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false
    )
    private Role role;


    // =========================================================
    // ACCOUNT STATUS
    //
    // ACTIVE
    // PENDING_APPROVAL
    // BLOCKED
    // =========================================================

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false
    )
    private AccountStatus status;


    // =========================================================
    // EMAIL VERIFICATION
    // =========================================================

    @Column(
            name = "email_verified",
            nullable = false
    )
    private Boolean emailVerified = false;


    // =========================================================
    // PASSWORD RESET TOKEN
    //
    // Used for Forgot Password functionality.
    //
    // Example:
    // 8c7a9e1b-xxxx-xxxx-xxxx
    // =========================================================

    @Column(
            name = "reset_token",
            unique = true
    )
    private String resetToken;


    // =========================================================
    // PASSWORD RESET TOKEN EXPIRY
    //
    // Example:
    // Current Time + 15 Minutes
    // =========================================================

    @Column(
            name = "reset_token_expiry"
    )
    private LocalDateTime resetTokenExpiry;


    // =========================================================
    // CREATED AT
    // =========================================================

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;


    // =========================================================
    // UPDATED AT
    // =========================================================

    @UpdateTimestamp
    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;


    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public User() {
    }


    // =========================================================
    // GET ID
    // =========================================================

    public UUID getId() {

        return id;
    }


    // =========================================================
    // SET ID
    // =========================================================

    public void setId(UUID id) {

        this.id = id;
    }


    // =========================================================
    // GET FIRST NAME
    // =========================================================

    public String getFirstName() {

        return firstName;
    }


    // =========================================================
    // SET FIRST NAME
    // =========================================================

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }


    // =========================================================
    // GET LAST NAME
    // =========================================================

    public String getLastName() {

        return lastName;
    }


    // =========================================================
    // SET LAST NAME
    // =========================================================

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }


    // =========================================================
    // GET EMAIL
    // =========================================================

    public String getEmail() {

        return email;
    }


    // =========================================================
    // SET EMAIL
    // =========================================================

    public void setEmail(String email) {

        this.email = email;
    }


    // =========================================================
    // GET PHONE NUMBER
    // =========================================================

    public String getPhoneNumber() {

        return phoneNumber;
    }


    // =========================================================
    // SET PHONE NUMBER
    // =========================================================

    public void setPhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;
    }


    // =========================================================
    // GET PASSWORD
    // =========================================================

    @Override
    public String getPassword() {

        return password;
    }


    // =========================================================
    // SET PASSWORD
    // =========================================================

    public void setPassword(String password) {

        this.password = password;
    }


    // =========================================================
    // GET ROLE
    // =========================================================

    public Role getRole() {

        return role;
    }


    // =========================================================
    // SET ROLE
    // =========================================================

    public void setRole(Role role) {

        this.role = role;
    }


    // =========================================================
    // GET ACCOUNT STATUS
    // =========================================================

    public AccountStatus getStatus() {

        return status;
    }


    // =========================================================
    // SET ACCOUNT STATUS
    // =========================================================

    public void setStatus(AccountStatus status) {

        this.status = status;
    }


    // =========================================================
    // GET EMAIL VERIFIED
    // =========================================================

    public Boolean getEmailVerified() {

        return emailVerified;
    }


    // =========================================================
    // SET EMAIL VERIFIED
    // =========================================================

    public void setEmailVerified(Boolean emailVerified) {

        this.emailVerified = emailVerified;
    }


    // =========================================================
    // GET RESET TOKEN
    // =========================================================

    public String getResetToken() {

        return resetToken;
    }


    // =========================================================
    // SET RESET TOKEN
    // =========================================================

    public void setResetToken(String resetToken) {

        this.resetToken = resetToken;
    }


    // =========================================================
    // GET RESET TOKEN EXPIRY
    // =========================================================

    public LocalDateTime getResetTokenExpiry() {

        return resetTokenExpiry;
    }


    // =========================================================
    // SET RESET TOKEN EXPIRY
    // =========================================================

    public void setResetTokenExpiry(
            LocalDateTime resetTokenExpiry
    ) {

        this.resetTokenExpiry = resetTokenExpiry;
    }


    // =========================================================
    // GET CREATED AT
    // =========================================================

    public LocalDateTime getCreatedAt() {

        return createdAt;
    }


    // =========================================================
    // GET UPDATED AT
    // =========================================================

    public LocalDateTime getUpdatedAt() {

        return updatedAt;
    }


    // =========================================================
    // SPRING SECURITY AUTHORITIES
    //
    // CUSTOMER -> ROLE_CUSTOMER
    // SELLER   -> ROLE_SELLER
    // ADMIN    -> ROLE_ADMIN
    // =========================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + role.name()
                )
        );
    }


    // =========================================================
    // SPRING SECURITY USERNAME
    //
    // Email is used as username.
    // =========================================================

    @Override
    public String getUsername() {

        return email;
    }


    // =========================================================
    // ACCOUNT NON EXPIRED
    // =========================================================

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }


    // =========================================================
    // ACCOUNT NON LOCKED
    // =========================================================

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }


    // =========================================================
    // CREDENTIALS NON EXPIRED
    // =========================================================

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }


    // =========================================================
    // ACCOUNT ENABLED
    //
    // ACTIVE          -> Can Login
    // PENDING_APPROVAL -> Cannot Login
    // BLOCKED         -> Cannot Login
    // =========================================================

    @Override
    public boolean isEnabled() {

        return status == AccountStatus.ACTIVE;
    }

}