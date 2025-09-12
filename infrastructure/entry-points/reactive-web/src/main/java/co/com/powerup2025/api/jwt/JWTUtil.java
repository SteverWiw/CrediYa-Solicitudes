package co.com.powerup2025.api.jwt;


import co.com.powerup2025.model.IJWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtil implements IJWTUtil {

    private final SecretKey secretKey;

    public JWTUtil() {
        String secretKeyString = "H8PVV7h2qr3AnAhpJ5vRCEHDbGP1Vm5R";
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String subject, String role) {
        return Jwts.builder()
                .subject(subject)
                .claim("authorities", List.of(role))
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(360, ChronoUnit.MINUTES)))
                .signWith(secretKey)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, String expectedUsername) {
        Claims claims = parseToken(token);
        return claims.getExpiration().after(Date.from(Instant.now())) &&
                claims.getSubject().equals(expectedUsername);
    }

    public List<String> getRoles(String token) {
        return parseToken(token).get("authorities", List.class);
    }

    @Override
    public String getUsername(String token) {
        return parseToken(token).getSubject();
    }
}


