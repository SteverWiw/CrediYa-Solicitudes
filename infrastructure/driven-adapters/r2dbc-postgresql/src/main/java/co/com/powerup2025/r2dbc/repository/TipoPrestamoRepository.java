package co.com.powerup2025.r2dbc.repository;


import co.com.powerup2025.r2dbc.Entity.TipoPrestamoEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TipoPrestamoRepository extends ReactiveCrudRepository<TipoPrestamoEntity, Integer>, ReactiveQueryByExampleExecutor<TipoPrestamoEntity> {
}
