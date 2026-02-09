package org.example.apigateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;
import java.util.List;


@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {
    private JwtUtil jwt;


    public JwtAuthFilter(JwtUtil jwt) {
        this.jwt = jwt;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        boolean isPublicPath = path.startsWith("/autenticacion")
                || path.equals("/usuarios/registrar")
                || path.equals("/usuarios/crear")
                || path.equals("/usuarios/auth-data");
        if (isPublicPath) {
            return chain.filter(exchange);
        }

        List<String> authHeaders = exchange.getRequest().getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION);
        if (authHeaders.isEmpty() || !authHeaders.get(0).startsWith("Bearer")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeaders.get(0).substring(7);

        try {
            var jws = jwt.parse(token);
            String username = jws.getBody().getSubject();

            Object rolesClaim = jws.getBody().get("roles");
            String rolesString = rolesClaim != null ? rolesClaim.toString() : "";

            // Impresion de autorizaciones para debbuging
            System.out.println("Gateway -> Usuario: " + username + " | Roles: " + rolesString);

            var mutated = exchange.getRequest().mutate()
                    .header("X-Auth-User", username)
                    .header("X-Auth-Roles", rolesString)
                    .build();

            return chain.filter(exchange.mutate().request(mutated).build());

        } catch (Exception e) {
            System.err.println("Error validando token en Gateway: " + e.getMessage());
            e.printStackTrace();

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }



    @Override
    public int getOrder() { return -1; }
}
