package co.com.powerup2025.model.solicitudes.gateways;

import co.com.powerup2025.model.solicitudes.Solicitudes;
import reactor.core.publisher.Mono;

public interface SolicitudesRepository {
    Mono<Solicitudes> save(Solicitudes solicitudes);
}
