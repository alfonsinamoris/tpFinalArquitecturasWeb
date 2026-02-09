package org.example.usuario.config;

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
                        // Endpoints Públicos (Login/Registro/Auth interna)
                        .requestMatchers(HttpMethod.POST, "/usuarios/registrar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/crear").permitAll()
                        .requestMatchers("/usuarios/auth-data").permitAll()

                        // Endpoint "/cercanos": Permitido para USER y ADMIN
                        .requestMatchers("/usuarios/cercanos").hasAnyRole("USER", "ADMIN")

                        // Cualquier otro endpoint requiere autenticación
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
