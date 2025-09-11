package co.com.powerup2025.model.loantype.gateways;

import co.com.powerup2025.model.loantype.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<LoanType> findById(Integer id);
}
