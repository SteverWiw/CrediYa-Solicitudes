package co.com.powerup2025.api.handler;

import co.com.powerup2025.api.mapper.SolicitudMapper;
import co.com.powerup2025.api.request_dto.SolicitudRequestDTO;
import co.com.powerup2025.errorhelper.ReactiveErrorHelper;
import co.com.powerup2025.model.exception.gateways.LoggerPort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup2025.model.solicitudes.gateways.SolicitudesService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SolicitudHandler {

    private final SolicitudesService solicitudesService;
    private final LoggerPort logger;
    private final SolicitudMapper mapper;
    private final ReactiveErrorHelper errorHelper;

    public Mono<ServerResponse> crearteLoan(ServerRequest request) {
        String traceId = UUID.randomUUID().toString();

        return request.bodyToMono(SolicitudRequestDTO.class)
                .doOnSubscribe(subscription -> logger.info("Iniciando creación de solicitud", traceId))
                .doOnNext(dto -> logger.info(String.format("Datos recibidos: %s", dto), traceId))
                .map(mapper::toEntity)
                .flatMap(solicitudesService::createLoan)
                .map(mapper::toDto)
                .flatMap(solicitudResponseDTO -> Mono.fromRunnable(() -> logger.info(String.format("Solicitud Creada %s", solicitudResponseDTO), traceId)).then(ServerResponse.ok().bodyValue(solicitudResponseDTO)))
                .doOnTerminate(() -> logger.info("Flujo finalizado", traceId))
                .onErrorResume(errorHelper::handle)
                .contextWrite(Context.of("traceId", traceId));
    }
}
