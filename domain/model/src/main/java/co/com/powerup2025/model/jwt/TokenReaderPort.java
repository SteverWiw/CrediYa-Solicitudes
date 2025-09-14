package co.com.powerup2025.model.jwt;

public interface TokenReaderPort {
    String getUsername(String token);
}
