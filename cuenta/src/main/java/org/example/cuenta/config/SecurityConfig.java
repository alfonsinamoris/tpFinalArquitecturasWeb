package org.example.cuenta.config;


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

                        .requestMatchers(HttpMethod.GET, "/cuenta/**").permitAll()
                        .requestMatchers("/cuenta/anular/**").hasRole("ADMIN")
                        .requestMatchers("/cuenta/cargarSaldo/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/cuenta").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/cuenta/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/cuenta/**").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
