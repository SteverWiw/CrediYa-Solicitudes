package co.com.powerup2025.logger.config;

import co.com.powerup2025.logger.LoggerPortAdapter;
import co.com.powerup2025.model.exception.gateways.LoggerFactoryPort;
import co.com.powerup2025.model.exception.gateways.LoggerPort;
import org.springframework.stereotype.Component;

@Component
public class LoggerFactoryPortImpl implements LoggerFactoryPort {

    @Override
    public LoggerPort getLogger(Class<?> clazz) {
        return new LoggerPortAdapter(clazz);
    }
}