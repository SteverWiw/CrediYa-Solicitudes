package co.com.powerup2025.r2dbc.adapters;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import co.com.powerup2025.model.solicitudes.Solicitudes;
import co.com.powerup2025.model.solicitudes.gateways.SolicitudesRepository;
import co.com.powerup2025.r2dbc.Entity.SolicitudEntity;
import co.com.powerup2025.r2dbc.helper.ReactiveAdapterOperations;
import co.com.powerup2025.r2dbc.repository.SolicitudRepository;

@Repository
public class SolicitudRepositoryAdapter
        extends ReactiveAdapterOperations<Solicitudes, SolicitudEntity, Long, SolicitudRepository>
        implements SolicitudesRepository {
    public SolicitudRepositoryAdapter(SolicitudRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Solicitudes.SolicitudesBuilder.class).build());
    }

}
