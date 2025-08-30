package co.com.powerup2025.api.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Hooks;


@Configuration
public class ContextConfig {
    @PostConstruct
    public void init() {
        // Habilita propagación automática de contexto (traceId, spanId, MDC, etc.)
        Hooks.enableAutomaticContextPropagation();
    }
}
