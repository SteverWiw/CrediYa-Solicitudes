package co.com.powerup2025.model.logger;

public interface LoggerFactoryPort {
    LoggerRepository getLogger(Class<?> clazz);
}