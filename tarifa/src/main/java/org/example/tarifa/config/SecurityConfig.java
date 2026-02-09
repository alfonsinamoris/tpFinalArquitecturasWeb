package org.example.tarifa.config;


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
                        // AJUSTE DE PRECIOS: Solo Admin (Punto 4.f y reglas generales)
                        .requestMatchers(HttpMethod.POST, "/tarifas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/tarifas/**").hasRole("ADMIN")
                        .requestMatchers("/tarifas/ajuste").hasRole("ADMIN")

                        // CONSULTA: Todos necesitan saber cuánto cuesta
                        .requestMatchers(HttpMethod.GET, "/tarifas/**").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                );
        return http.build();
    }
}