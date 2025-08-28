package co.com.powerup2025.errorhelper;

import co.com.powerup2025.errorhelper.dto.ErrorResponse;
import co.com.powerup2025.errorhelper.mapper.ErrorCodeMapper;
import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.exception.gateways.LoggerPort;
import co.com.powerup2025.model.exception.gateways.iErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ReactiveErrorHelper {
    private final LoggerPort logger;

    public ReactiveErrorHelper(LoggerPort logger) {
        this.logger = logger;
    }

    public Mono<ServerResponse> handle(Throwable ex) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "NO_TRACE");

            List<iErrorCode> errores = (ex instanceof BusinessException be)
                    ? be.getErrorCodes()
                    : List.of(ErrorCode.SYS_001);

            HttpStatus status = ErrorCodeMapper.mapToHttpStatus(errores.get(0));

            errores.forEach(error -> logger.error(error.message(), ex, traceId));

            Object responseBody = errores.size() == 1
                    ? new ErrorResponse(errores.get(0), traceId)
                    : errores.stream().map(error -> new ErrorResponse(error, traceId)).toList();

            return ServerResponse.status(status).bodyValue(responseBody);
        });
    }
}