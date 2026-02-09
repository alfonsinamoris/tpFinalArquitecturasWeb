package org.example.config;

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
                        // CONSULTAS: Permitido para Usuarios y Administradores
                        .requestMatchers(HttpMethod.GET, "/paradas/**").hasAnyRole("USER", "ADMIN")

                        // ALTAS Y BAJAS (Crear, Eliminar, Ubicar monopatin): SOLO ADMIN
                        .requestMatchers(HttpMethod.POST, "/paradas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/paradas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/paradas/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                );

        return http.build();
    }
}