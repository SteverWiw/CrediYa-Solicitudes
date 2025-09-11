package co.com.powerup2025.model.logger;

import reactor.core.publisher.Mono;

public interface LoggerRepository {
    Mono<Void> info(String message);
    Mono<Void> warn(String message);
    Mono<Void> error(String message);
    Mono<Void> error(String message, Throwable throwable);

}
