package co.com.powerup2025.api.handlers;

import co.com.powerup2025.api.dtos.response.ErrorResponse;
import co.com.powerup2025.api.mapper.ErrorCodeMapper;
import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.exception.exceptions.InvalidCredentialsException;
import co.com.powerup2025.model.exception.gateways.IErrorCode;
import co.com.powerup2025.model.logger.LoggerFactoryPort;
import co.com.powerup2025.model.logger.LoggerRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class ReactiveErrorHandler {

    private final LoggerRepository logger;
    private final Tracer tracer;



    public ReactiveErrorHandler(LoggerFactoryPort loggerFactory, Tracer tracer) {
        this.logger = loggerFactory.getLogger(ReactiveErrorHandler.class);
        this.tracer = tracer;
    }

    public Mono<ServerResponse> handle(Throwable ex) {
        return Mono.deferContextual(ctx -> {
            Span currentSpan = tracer.currentSpan();
            String traceId = currentSpan != null ? currentSpan.context().traceId() : "no-trace";

            HttpStatus status;
            List<ErrorResponse> responseBody;

            if (ex instanceof BusinessException be) {
                List<IErrorCode> errores = be.getErrorCodes();
                status = ErrorCodeMapper.mapToHttpStatus(errores.get(0));
                errores.forEach(error -> logger.error(error.message(), ex));
                responseBody = errores.stream()
                        .map(error -> new ErrorResponse(error, traceId))
                        .toList();

            } else if (ex instanceof ConstraintViolationException cve) {
                status = HttpStatus.BAD_REQUEST;
                logger.error("Violaciones de validación", ex);
                responseBody = cve.getConstraintViolations().stream()
                        .map(violation -> new ErrorResponse(
                                "VALIDATION",
                                violation.getPropertyPath() + ": " + violation.getMessage(),
                                "validation",
                                "LOW",
                                traceId
                        ))
                        .toList();

            }  else {
                IErrorCode error;
                if (ex instanceof BadCredentialsException) {
                    error = ErrorCode.AUT_001;
                } else if (ex instanceof InvalidCredentialsException) {
                    error = ErrorCode.AUT_002;
                }
                else if (ex instanceof UsernameNotFoundException) {
                    error = ErrorCode.USR_001;
                } else if (ex instanceof AccessDeniedException) {
                    error = ErrorCode.AUT_002;
                } else if(ex instanceof AuthenticationException) {
                    error = ErrorCode.AUT_003;
                }else {
                    error = ErrorCode.SYS_001;
                }

                status = ErrorCodeMapper.mapToHttpStatus(error);
                logger.error(error.message(), ex);
                responseBody = List.of(new ErrorResponse(error, traceId));
            }

            return ServerResponse.status(status).bodyValue(responseBody);
        });
    }
}
