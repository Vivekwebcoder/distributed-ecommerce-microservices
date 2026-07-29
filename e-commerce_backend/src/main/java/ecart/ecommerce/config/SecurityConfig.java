package ecart.ecommerce.config;

import ecart.ecommerce.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    // =========================================================
    // PASSWORD ENCODER
    // =========================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    // =========================================================
    // CORS CONFIGURATION
    // =========================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        // React Frontend
        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173"
                )
        );

        // Allowed HTTP Methods
        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )
        );

        // Allow All Headers
        configuration.setAllowedHeaders(
                List.of("*")
        );

        // Allow Credentials
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }


    // =========================================================
    // SECURITY FILTER CHAIN
    // =========================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // =================================================
                // CORS
                // =================================================

                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource()
                        )
                )


                // =================================================
                // CSRF DISABLED
                // =================================================

                .csrf(csrf ->
                        csrf.disable()
                )


                // =================================================
                // FORM LOGIN DISABLED
                // =================================================

                .formLogin(form ->
                        form.disable()
                )


                // =================================================
                // HTTP BASIC DISABLED
                // =================================================

                .httpBasic(basic ->
                        basic.disable()
                )


                // =================================================
                // STATELESS SESSION
                // =================================================

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                // =================================================
                // EXCEPTION HANDLING
                // =================================================

                .exceptionHandling(exception -> exception


                        // =================================================
                        // 401 - UNAUTHORIZED
                        // =================================================

                        .authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    );

                                    response.setContentType(
                                            "application/json"
                                    );

                                    response.getWriter().write(
                                            "{\"message\":\"Unauthorized - JWT token is required\"}"
                                    );
                                }
                        )


                        // =================================================
                        // 403 - ACCESS DENIED
                        // =================================================

                        .accessDeniedHandler(
                                (
                                        request,
                                        response,
                                        accessDeniedException
                                ) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_FORBIDDEN
                                    );

                                    response.setContentType(
                                            "application/json"
                                    );

                                    response.getWriter().write(
                                            "{\"message\":\"Access Denied - You do not have permission to access this resource\"}"
                                    );
                                }
                        )
                )


                // =================================================
                // AUTHORIZATION RULES
                // =================================================

                .authorizeHttpRequests(auth -> auth


                        // =================================================
                        // REGISTER - PUBLIC
                        // =================================================

                        .requestMatchers(
                                "/api/auth/register"
                        )
                        .permitAll()


                        // =================================================
                        // LOGIN - PUBLIC
                        // =================================================

                        .requestMatchers(
                                "/api/auth/login"
                        )
                        .permitAll()


                        // =================================================
                        // FORGOT PASSWORD - PUBLIC
                        // =================================================

                        .requestMatchers(
                                "/api/auth/forgot-password"
                        )
                        .permitAll()


                        // =================================================
                        // RESET PASSWORD - PUBLIC
                        // =================================================

                        .requestMatchers(
                                "/api/auth/reset-password"
                        )
                        .permitAll()


                        // =================================================
                        // ALL OTHER APIs - JWT REQUIRED
                        // =================================================

                        .anyRequest()
                        .authenticated()
                )


                // =================================================
                // JWT AUTHENTICATION FILTER
                // =================================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }
}