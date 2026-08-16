package com.ecommerce.productservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String xUserIdHeader = request.getHeader("X-User-Id");

        if ((authHeader == null || !authHeader.startsWith("Bearer ")) && xUserIdHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtService.isTokenValid(token)) {
                    String username = jwtService.extractUsername(token);
                    String tokenUserId = jwtService.extractUserId(token);
                    String role = jwtService.extractRole(token);

                    if (tokenUserId == null && xUserIdHeader != null) {
                        tokenUserId = xUserIdHeader;
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
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
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
            log.error("JWT Authentication failed in product-service: {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
