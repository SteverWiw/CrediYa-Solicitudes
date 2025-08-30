package co.com.powerup2025.consumer;

import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import reactor.core.publisher.Mono;

@Component
public class IntegrationErrorHandler {

    public <T> Mono<T> handleIntegrationError(Throwable ex, String moduleName) {
        if (ex instanceof TimeoutException) {
            return Mono.error(new BusinessException(ErrorCode.INT_002.withModule(moduleName)));
        } else if (ex instanceof UnknownHostException) {
            return Mono.error(new BusinessException(ErrorCode.INT_003.withModule(moduleName)));
        } else if (ex instanceof WebClientResponseException wex) {
            if (wex.getStatusCode().is5xxServerError()) {
                return Mono.error(new BusinessException(ErrorCode.INT_006.withModule(moduleName)));
            } else if (wex.getStatusCode().is4xxClientError()) {
                return Mono.error(new BusinessException(ErrorCode.INT_007.withModule(moduleName)));
            }
        } else if (ex instanceof CallNotPermittedException) {
            return Mono.error(new BusinessException(ErrorCode.INT_008.withModule(moduleName)));
        }

        return Mono.error(new BusinessException(ErrorCode.INT_001.withModule(moduleName)));
    }
}
