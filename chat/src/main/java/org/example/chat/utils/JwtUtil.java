package org.example.chat.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET = "ClaveSecretaSuperLargaAbcdefghijk123456789";
    private final Key key;

    public JwtUtil() {
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
    public String extractUsername(String token) {
        // Obtiene el cuerpo del token (Claims) y extrae el "Subject" (el ID del usuario)
        return parse(token).getBody().getSubject();
    }

    public String createToken(String subject, String roles) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 604800000L; // 1 semana de duracion

        return Jwts.builder()
                .setSubject(subject)
                .claim("roles", roles)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(expMillis))
                .signWith(key) // Firma con la clave secreta
                .compact();
    }
}
