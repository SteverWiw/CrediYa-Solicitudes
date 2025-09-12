package co.com.powerup2025.model.loans.gateways;

import co.com.powerup2025.model.loans.Loan;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ILoanUseCase {
    Mono <Loan> createLoan(Loan loan,String token);

    Flux<Loan> listarSolicitudes(String estado);


}
