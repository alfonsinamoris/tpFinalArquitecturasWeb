package org.example.monopatin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, GatewayHeadersAuthFilter authFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // Permitir a USER y ADMIN hacer PUT (para cambiar estado al viajar)
                        .requestMatchers(HttpMethod.PUT, "/monopatines/**").hasAnyRole("USER", "ADMIN")

                        // Buscar en paradas (POST específico)
                        .requestMatchers(HttpMethod.POST, "/monopatines/en-paradas").hasAnyRole("USER", "ADMIN")

                        // El resto de POST y DELETE solo para ADMIN
                        .requestMatchers(HttpMethod.POST, "/monopatines/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/monopatines/**").hasRole("ADMIN")

                        // GET para todos
                        .requestMatchers(HttpMethod.GET, "/monopatines/**").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                );
        return http.build();
    }
}