package co.com.powerup2025.api.jwt;


import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

import co.com.powerup2025.model.jwt.TokenReaderPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenReaderAdapter implements TokenReaderPort {

    private final SecretKey secretKey;

    public JwtTokenReaderAdapter(@Value("${security.jwt.secret}") String rawSecret) {
        this.secretKey = Keys.hmacShaKeyFor(rawSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}



