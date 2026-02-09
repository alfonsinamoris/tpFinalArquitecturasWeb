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
                        // REPORTE TOTAL FACTURADO: Solo Admin (Punto 4.d)
                        .requestMatchers("/facturas/total-facturado").hasRole("ADMIN")

                        // CONSULTAS DE FACTURAS: Usuario y Admin
                        .requestMatchers(HttpMethod.GET, "/facturas/**").hasAnyRole("USER", "ADMIN")

                        // GENERAR FACTURA: llamado por Viaje, pero si entra por Gateway requiere auth.
                        .requestMatchers(HttpMethod.POST, "/facturas").authenticated()

                        .anyRequest().authenticated()
                );
        return http.build();
    }
}