package co.com.powerup2025.logger;


import co.com.powerup2025.model.exception.gateways.LoggerPort;
import org.slf4j.Logger;

import reactor.core.publisher.Mono;

public class ReactiveLoggerAdapter implements LoggerPort {
    private final Logger logger;

    public ReactiveLoggerAdapter() {
        this.logger = org.slf4j.LoggerFactory.getLogger(ReactiveLoggerAdapter.class);
    }

    public Mono<Void> info(String message) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "N/A");
            logger.info("[{}] {}", traceId, message);
            return Mono.empty();
        });
    }

    public Mono<Void> info(String message, String contextId) {
        logger.info("[{}] {}", contextId, message);
        return Mono.empty();
    }

    public Mono<Void> warn(String message) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "N/A");
            logger.warn("[{}] {}", traceId, message);
            return Mono.empty();
        });
    }

    public Mono<Void> error(String message, Throwable throwable) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "N/A");
            logger.error("[{}] {}", traceId, message, throwable);
            return Mono.empty();
        });
    }

    public Mono<Void> error(String message, Throwable throwable, String contextId) {
        logger.error("[{}] {}", contextId, message, throwable);
        return Mono.empty();
    }
}