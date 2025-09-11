package co.com.powerup2025.logger;

import co.com.powerup2025.model.logger.LoggerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class LoggerRepositoryAdapter implements LoggerRepository {
    private final Logger logger;

    public LoggerRepositoryAdapter(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public Mono<Void> info(String message) {
        logger.info("{}", message);
        return Mono.empty();
    }

    @Override
    public Mono<Void> warn(String message) {
        logger.warn("{}", message);
        return Mono.empty();
    }

    @Override
    public Mono<Void> error(String message) {
        logger.error("{}", message);
        return Mono.empty();
    }

    @Override
    public Mono<Void> error(String message, Throwable throwable) {
        logger.error("{}", message);
        logger.error("Full exception trace", throwable);
        return Mono.empty();
    }


}
