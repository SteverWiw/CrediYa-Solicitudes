package co.com.powerup2025.r2dbc.adapters;

import co.com.powerup2025.r2dbc.repository.LoanTypeReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import co.com.powerup2025.model.loantype.LoanType;
import co.com.powerup2025.model.loantype.gateways.LoanTypeRepository;
import co.com.powerup2025.r2dbc.Entity.LoanTypeEntity;
import co.com.powerup2025.r2dbc.helper.ReactiveAdapterOperations;

@Repository
public class LoanTypeReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<LoanType, LoanTypeEntity, Integer, LoanTypeReactiveRepository>
        implements LoanTypeRepository {
    public LoanTypeReactiveRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, LoanType.LoanTypeBuilder.class).build());
    }
}
