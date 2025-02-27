package com.mobsolution.spring_app.config.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.mobsolution.spring_app.config.security.utils.JwtRequestFilter;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtRequestFilter jwtRequestFilter;
  
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{


        http.authorizeHttpRequests(auth -> auth

            .requestMatchers("/api/presenca").authenticated()
            .requestMatchers("/api/participante").authenticated()
            .requestMatchers("/api/evento").authenticated()
            .requestMatchers( "/api/auth/login/**").permitAll()
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() 
            .anyRequest().authenticated()
            
        )
        .csrf(csrf -> csrf.disable())
        .exceptionHandling(exceptionHandling -> 
        exceptionHandling
            .authenticationEntryPoint(
                new AuthenticationEntryPointHandler()
            )
        )
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
       
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
