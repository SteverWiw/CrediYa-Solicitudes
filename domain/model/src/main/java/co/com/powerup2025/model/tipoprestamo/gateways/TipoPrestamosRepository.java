package co.com.powerup2025.model.tipoprestamo.gateways;

import co.com.powerup2025.model.tipoprestamo.TipoPrestamo;
import reactor.core.publisher.Mono;

public interface TipoPrestamosRepository {
    Mono<TipoPrestamo> findById(Integer id);
}
