package co.com.powerup2025.model.loans.gateways;

import co.com.powerup2025.model.loans.Loan;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanRepository {
    Mono<Loan> save(Loan loan);
    Flux<Loan> obtenerPorEstado(String estado);


}
