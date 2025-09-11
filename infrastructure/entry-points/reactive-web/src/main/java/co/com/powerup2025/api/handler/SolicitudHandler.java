package co.com.powerup2025.api.handler;

import co.com.powerup2025.api.mapper.LoanMapper;
import co.com.powerup2025.api.dtos.request.LoanRequest;
import co.com.powerup2025.model.exception.gateways.LoggerFactoryPort;
import co.com.powerup2025.model.exception.gateways.LoggerPort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup2025.model.solicitudes.gateways.SolicitudesService;
import reactor.core.publisher.Mono;

@Component
public class SolicitudHandler {


    private final SolicitudesService solicitudesService;
    private final LoggerPort logger;
    private final LoanMapper mapper;
    private final ReactiveErrorHandler errorHelper;

    public SolicitudHandler(SolicitudesService solicitudesService, LoggerFactoryPort loggerFactoryPort, LoanMapper mapper, ReactiveErrorHandler errorHelper) {
        this.solicitudesService = solicitudesService;
        this.logger = loggerFactoryPort.getLogger(SolicitudHandler.class);
        this.mapper = mapper;
        this.errorHelper = errorHelper;
    }

    public Mono<ServerResponse> crearteLoan(ServerRequest request) {
       

        return request.bodyToMono(LoanRequest.class)
                .doFirst(() -> logger.info("Iniciando creación de solicitud"))
                .doOnNext(dto -> logger.info(String.format("Datos recibidos: %s", dto)))
                .map(mapper::toEntity)
                .flatMap(solicitudesService::createLoan)
                .map(mapper::toDto)
                .flatMap(solicitudResponseDTO -> Mono.fromRunnable(() -> logger.info(String.format("Solicitud Creada %s", solicitudResponseDTO))).then(ServerResponse.ok().bodyValue(solicitudResponseDTO)))
                .doOnTerminate(() -> logger.info("Flujo finalizado"))
                .onErrorResume(errorHelper::handle);
    }
}
