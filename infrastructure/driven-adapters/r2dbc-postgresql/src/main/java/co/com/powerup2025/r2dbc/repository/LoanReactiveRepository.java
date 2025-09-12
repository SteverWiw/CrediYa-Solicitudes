package co.com.powerup2025.r2dbc.repository;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.com.powerup2025.r2dbc.Entity.LoanEntity;
import reactor.core.publisher.Flux;

public interface LoanReactiveRepository
                extends ReactiveCrudRepository<LoanEntity, Long>, ReactiveQueryByExampleExecutor<LoanEntity> {

    Flux<LoanEntity> findAllByIdEstado(String idEstado);

}
