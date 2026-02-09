package org.example.viaje.config;

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

                        .requestMatchers("/viajes/reporte-uso").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/viajes/reportes/**").hasRole("ADMIN")

                        .requestMatchers("/viajes/iniciar").hasRole("USER")
                        .requestMatchers("/viajes/finalizar/**").hasRole("USER")
                        .requestMatchers("/pausas/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/viajes/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/error").permitAll()

                        .anyRequest().authenticated()
                );
        return http.build();
    }
}