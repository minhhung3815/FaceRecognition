package com.face.recognittion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import com.face.recognittion.service.TokenService;
import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Autowired
    private TokenService tokenService;

    private static final String[] PATH_WHITELIST = {
        "/api/auth/login",
        "/api/admins/create-user",
        "/api/admins/create-classes",
        "/api/admins/assign-student",
        "/api/students/classes",
        "/api/professors/classes/details"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors() // Enable CORS
            .and()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PATH_WHITELIST).permitAll() // Allow login and register endpoints using regex
                .anyRequest().authenticated() // Protect all other endpoints
            )
            .addFilterBefore(new TokenAuthenticationFilter(tokenService), BasicAuthenticationFilter.class); // Add custom filter

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // Allow all origins (adjust as needed)
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials (optional)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply CORS settings to all endpoints
        return source;
    }

    private class TokenAuthenticationFilter extends OncePerRequestFilter {

        private final TokenService tokenService;

        public TokenAuthenticationFilter(TokenService tokenService) {
            this.tokenService = tokenService;
        }

        @Override
        protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
                throws jakarta.servlet.ServletException, IOException {
                    String token = request.getHeader("Authorization");
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7); // Remove "Bearer " prefix
                        if (tokenService.validateToken(token)) {
                            String userId = tokenService.getUserIdFromToken(token);
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userId, null, null); // Set user details and authorities if needed
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                    filterChain.doFilter(request, response);
        }
    }
}
