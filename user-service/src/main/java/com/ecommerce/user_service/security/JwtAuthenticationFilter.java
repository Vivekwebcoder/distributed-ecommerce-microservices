package com.ecommerce.user_service.security;

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

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (jwtService.isTokenValid(token)) {
                String username = jwtService.extractUsername(token);
                Long tokenUserId = jwtService.extractUserId(token);
                String role = jwtService.extractRole(token);

                Long finalUserId = tokenUserId;

                if (xUserIdHeader != null && !xUserIdHeader.isBlank()) {
                    try {
                        Long headerUserId = Long.parseLong(xUserIdHeader);
                        if (tokenUserId != null && !tokenUserId.equals(headerUserId)) {
                            log.warn("Security Alert: Header X-User-Id ({}) does not match JWT claim userId ({})", headerUserId, tokenUserId);
                            filterChain.doFilter(request, response);
                            return;
                        }
                        if (finalUserId == null) {
                            finalUserId = headerUserId;
                        }
                    } catch (NumberFormatException e) {
                        log.warn("Invalid X-User-Id header format: {}", xUserIdHeader);
                        filterChain.doFilter(request, response);
                        return;
                    }
                }

                if (finalUserId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    if (role != null && !role.isBlank()) {
                        String roleName = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                        authorities.add(new SimpleGrantedAuthority(roleName));
                    } else {
                        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    }

                    UserPrincipal userPrincipal = UserPrincipal.builder()
                            .userId(finalUserId)
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
        } catch (Exception ex) {
            log.error("JWT Authentication failed: {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
