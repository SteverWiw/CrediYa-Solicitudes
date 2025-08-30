package co.com.powerup2025.api.handler;

import co.com.powerup2025.api.mapper.ErrorCodeMapper;
import co.com.powerup2025.api.response_dto.ErrorResponse;
import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.exception.gateways.LoggerFactoryPort;
import co.com.powerup2025.model.exception.gateways.LoggerPort;
import co.com.powerup2025.model.exception.gateways.iErrorCode;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ReactiveErrorHandler {
    private final LoggerPort logger;
    private final Tracer tracer;

    public ReactiveErrorHandler(LoggerFactoryPort loggerFactory, Tracer tracer) {
        this.logger = loggerFactory.getLogger(ReactiveErrorHandler.class);
        this.tracer = tracer;
    }

    public Mono<ServerResponse> handle(Throwable ex) {
        return Mono.deferContextual(ctx -> {
            Span currentSpan = tracer.currentSpan();
            String traceId = currentSpan.context().traceId();

            List<iErrorCode> errores = (ex instanceof BusinessException be)
                    ? be.getErrorCodes()
                    : List.of(ErrorCode.SYS_001);

            HttpStatus status = ErrorCodeMapper.mapToHttpStatus(errores.get(0));

            errores.forEach(error -> logger.error(error.message(), ex));



            List<ErrorResponse> responseBody = errores.stream()
                    .map(error -> new ErrorResponse(error, traceId))
                    .toList();

            return ServerResponse.status(status).bodyValue(responseBody);
        });
    }
}
