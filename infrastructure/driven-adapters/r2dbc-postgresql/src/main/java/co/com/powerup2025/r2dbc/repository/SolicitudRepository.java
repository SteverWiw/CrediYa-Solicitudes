package co.com.powerup2025.r2dbc.repository;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.com.powerup2025.r2dbc.Entity.SolicitudEntity;

public interface SolicitudRepository
                extends ReactiveCrudRepository<SolicitudEntity, Long>, ReactiveQueryByExampleExecutor<SolicitudEntity> {

}
