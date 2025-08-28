package co.com.powerup2025.api;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup2025.model.solicitudes.gateways.SolicitudesService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SolicitudHandler {

    private final SolicitudesService solicitudesService;

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest request) {
        return ServerResponse.ok().bodyValue("Hola desde SolicitudHandler");
    }
}
