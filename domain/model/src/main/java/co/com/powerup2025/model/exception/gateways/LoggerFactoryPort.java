package co.com.powerup2025.model.exception.gateways;

public interface LoggerFactoryPort {
    LoggerPort getLogger(Class<?> clazz);
}