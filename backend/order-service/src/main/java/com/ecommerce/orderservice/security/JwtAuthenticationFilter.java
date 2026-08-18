package com.ecommerce.orderservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String xUserIdHeader = request.getHeader("X-User-Id");
        String xUserRoleHeader = request.getHeader("X-User-Role");

        if ((authHeader == null || !authHeader.startsWith("Bearer ")) && xUserIdHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtTokenProvider.isTokenValid(token)) {
                    String username = jwtTokenProvider.extractUsername(token);
                    String tokenUserId = jwtTokenProvider.extractUserId(token);
                    String role = jwtTokenProvider.extractRole(token);

                    if (tokenUserId == null && xUserIdHeader != null) {
                        tokenUserId = xUserIdHeader;
                    }

                    if (role == null && xUserRoleHeader != null) {
                        role = xUserRoleHeader;
                    }

                    if (SecurityContextHolder.getContext().getAuthentication() == null && tokenUserId != null) {
                        List<GrantedAuthority> authorities = new ArrayList<>();
                        if (role != null && !role.isBlank()) {
                            String roleName = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                            authorities.add(new SimpleGrantedAuthority(roleName));
                        } else {
                            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                        }

                        UserPrincipal userPrincipal = UserPrincipal.builder()
                                .userId(tokenUserId)
                                .email(username)
                                .authorities(authorities)
                                .build();

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userPrincipal,
                                null,
                                userPrincipal.getAuthorities()
                        );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } else if (xUserIdHeader != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                if (xUserRoleHeader != null && !xUserRoleHeader.isBlank()) {
                    String roleName = xUserRoleHeader.startsWith("ROLE_") ? xUserRoleHeader : "ROLE_" + xUserRoleHeader;
                    authorities.add(new SimpleGrantedAuthority(roleName));
                } else {
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                }

                UserPrincipal userPrincipal = UserPrincipal.builder()
                        .userId(xUserIdHeader)
                        .email("user" + xUserIdHeader + "@ecommerce.com")
                        .authorities(authorities)
                        .build();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userPrincipal,
                        null,
                        userPrincipal.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("JWT Authentication failed in order-service: {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
