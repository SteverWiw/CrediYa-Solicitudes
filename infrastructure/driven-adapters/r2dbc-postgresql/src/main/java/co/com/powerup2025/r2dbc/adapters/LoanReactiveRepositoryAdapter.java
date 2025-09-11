package co.com.powerup2025.r2dbc.adapters;

import co.com.powerup2025.model.loans.Loan;
import co.com.powerup2025.model.loans.gateways.LoanRepository;
import co.com.powerup2025.r2dbc.repository.LoanReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

import co.com.powerup2025.r2dbc.Entity.LoanEntity;
import co.com.powerup2025.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Mono;

@Repository
public class LoanReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<Loan, LoanEntity, Long, LoanReactiveRepository>
        implements LoanRepository {

    private final TransactionalOperator txOperator;

    public LoanReactiveRepositoryAdapter(LoanReactiveRepository repository, ObjectMapper mapper,
                                         TransactionalOperator txOperator) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Loan.LoanBuilder.class).build());
        this.txOperator = txOperator;
    }

    @Override
    public Mono<Loan> save(Loan loan) {
        return Mono.just(loan)
                .map(s -> mapper.mapBuilder(s, LoanEntity.LoanEntityBuilder.class).build())
                .flatMap(entity -> repository.save(entity))
                .flatMap(saved -> repository.findById(saved.getIdSolicitud()))
                .map(entity -> mapper.mapBuilder(entity, Loan.LoanBuilder.class).build()) // mapeo a
                                                                                                        // modelo
                .as(txOperator::transactional);
    }

}
