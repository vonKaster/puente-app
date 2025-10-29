package com.puente.app.infrastructure.security;

import com.puente.app.domain.user.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}") // 24hs
    private long validityInMilliseconds;

    private SecretKey key;

    @PostConstruct
    protected void init() {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        }
    }

    public String createToken(String email, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles.stream()
            .map(role -> role.getName().name())
            .toList());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

