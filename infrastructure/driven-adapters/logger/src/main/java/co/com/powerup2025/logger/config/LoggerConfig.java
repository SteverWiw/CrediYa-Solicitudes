package co.com.powerup2025.logger.config;

import co.com.powerup2025.logger.ReactiveLoggerAdapter;
import co.com.powerup2025.model.exception.gateways.LoggerPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {

    @Bean
    public LoggerPort loggerPort() {
        Logger logger = LoggerFactory.getLogger(ReactiveLoggerAdapter.class);
        return new ReactiveLoggerAdapter();
    }
}