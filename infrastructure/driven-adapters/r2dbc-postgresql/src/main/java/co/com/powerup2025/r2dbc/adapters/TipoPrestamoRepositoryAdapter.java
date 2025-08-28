package co.com.powerup2025.r2dbc.adapters;

import co.com.powerup2025.model.tipoprestamo.TipoPrestamo;
import co.com.powerup2025.model.tipoprestamo.gateways.TipoPrestamosRepository;
import co.com.powerup2025.r2dbc.Entity.TipoPrestamoEntity;
import co.com.powerup2025.r2dbc.helper.ReactiveAdapterOperations;
import co.com.powerup2025.r2dbc.repository.TipoPrestamoRepository;
import org.reactivecommons.utils.ObjectMapper;

public class TipoPrestamoRepositoryAdapter extends ReactiveAdapterOperations<TipoPrestamo, TipoPrestamoEntity, Integer, TipoPrestamoRepository>
        implements TipoPrestamosRepository {
    public TipoPrestamoRepositoryAdapter(TipoPrestamoRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, TipoPrestamo.TipoPrestamoBuilder.class).build());
    }
}
