package co.com.powerup2025.logger.config;

import co.com.powerup2025.logger.LoggerRepositoryAdapter;
import co.com.powerup2025.model.logger.LoggerFactoryPort;
import co.com.powerup2025.model.logger.LoggerRepository;
import org.springframework.stereotype.Component;

@Component
public class LoggerFactoryPortImpl implements LoggerFactoryPort {

    @Override
    public LoggerRepository getLogger(Class<?> clazz) {
        return new LoggerRepositoryAdapter(clazz);
    }
}