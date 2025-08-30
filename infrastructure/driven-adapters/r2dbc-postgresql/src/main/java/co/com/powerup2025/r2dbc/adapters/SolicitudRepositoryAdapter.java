package co.com.powerup2025.r2dbc.adapters;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

import co.com.powerup2025.model.solicitudes.Solicitudes;
import co.com.powerup2025.model.solicitudes.gateways.SolicitudesRepository;
import co.com.powerup2025.r2dbc.Entity.SolicitudEntity;
import co.com.powerup2025.r2dbc.helper.ReactiveAdapterOperations;
import co.com.powerup2025.r2dbc.repository.SolicitudRepository;
import reactor.core.publisher.Mono;

@Repository
public class SolicitudRepositoryAdapter
        extends ReactiveAdapterOperations<Solicitudes, SolicitudEntity, Long, SolicitudRepository>
        implements SolicitudesRepository {

    private final TransactionalOperator txOperator;

    public SolicitudRepositoryAdapter(SolicitudRepository repository, ObjectMapper mapper,
            TransactionalOperator txOperator) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Solicitudes.SolicitudesBuilder.class).build());
        this.txOperator = txOperator;
    }

    @Override
    public Mono<Solicitudes> save(Solicitudes solicitudes) {
        return Mono.just(solicitudes)
                .map(s -> mapper.mapBuilder(s, SolicitudEntity.SolicitudEntityBuilder.class).build())
                .flatMap(entity -> repository.save(entity))
                .flatMap(saved -> repository.findById(saved.getIdSolicitud()))
                .map(entity -> mapper.mapBuilder(entity, Solicitudes.SolicitudesBuilder.class).build()) // mapeo a
                                                                                                        // modelo
                .as(txOperator::transactional);
    }

}
