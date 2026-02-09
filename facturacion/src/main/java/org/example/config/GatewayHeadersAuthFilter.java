package org.example.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GatewayHeadersAuthFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String rolesHeader = request.getHeader("X-Auth-Roles");
        String userHeader  = request.getHeader("X-Auth-User");

        System.out.println("=== FILTRO SEGURIDAD ADMINISTRADOR ===");
        System.out.println("URL Solicitada: " + request.getRequestURI());
        System.out.println("Header X-Auth-User: " + userHeader);
        System.out.println("Header X-Auth-Roles: " + rolesHeader);


        if (rolesHeader != null && !rolesHeader.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {

                String rolesClean = rolesHeader.replace("[", "").replace("]", "").replace("\"", "");

                List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesClean.split(","))
                        .map(String::trim)
                        .filter(role -> !role.isEmpty())
                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                var auth = new UsernamePasswordAuthenticationToken(
                        (userHeader != null ? userHeader : "anonymous"),
                        null,
                        authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println(">> Autenticación exitosa. Roles asignados: " + authorities);

            } catch (Exception e) {
                System.err.println(">> Error procesando roles: " + e.getMessage());
            }
        } else {
            System.out.println(">> NO se estableció autenticación (Headers nulos o vacíos).");
        }

        filterChain.doFilter(request, response);
    }
}
