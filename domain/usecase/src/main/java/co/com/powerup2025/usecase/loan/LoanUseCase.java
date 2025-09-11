package co.com.powerup2025.usecase.loan;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.exception.gateways.LoggerFactoryPort;
import co.com.powerup2025.model.exception.gateways.LoggerPort;
import co.com.powerup2025.model.loans.Loan;
import co.com.powerup2025.model.loans.enums.LoanState;
import co.com.powerup2025.model.loans.gateways.LoanRepository;
import co.com.powerup2025.model.loans.gateways.ILoanUseCase;
import co.com.powerup2025.model.loantype.gateways.LoanTypeRepository;
import co.com.powerup2025.model.user.gateways.UserRepository;
import co.com.powerup2025.usecase.loan.validations.LoanValidator;

import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;


public class LoanUseCase implements ILoanUseCase {

    private final UserRepository usuarioRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final LoanRepository loanRepository;
    private final LoggerPort logger;

    public LoanUseCase(UserRepository usuarioRepository, LoanTypeRepository loanTypeRepository, LoanRepository loanRepository, LoggerFactoryPort logger) {
        this.usuarioRepository = usuarioRepository;
        this.loanTypeRepository = loanTypeRepository;
        this.loanRepository = loanRepository;
        this.logger = logger.getLogger(LoanUseCase.class);
    }

    @Override
    public Mono<Loan> createLoan(Loan loan) {

        return LoanValidator.validar(loan)
                .flatMap(s -> logger.info("Validando datos de solicitud").thenReturn(s))
                .flatMap(validated ->
                        loanTypeRepository.findById(validated.getIdTipoPrestamo())
                                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.VAL_013)))
                                .flatMap(tipoPrestamo ->
                                        LoanValidator.validarMontoYPlazo(loan, tipoPrestamo)
                                                .flatMap(sl -> logger.info("Validando datos de prestamo").thenReturn(sl))
                                                .flatMap(s ->
                                                        usuarioRepository.getUserByEmail(loan.getEmail())
                                                                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.USR_001)))
                                                                .flatMap(usuario -> {
                                                                            String codigoSolicitud = generarCodigoSolicitud();

                                                                            Loan loanFinal = loan.toBuilder()
                                                                                    .codigoSolicitud(codigoSolicitud)
                                                                                    .monto(loan.getMonto())
                                                                                    .plazo(loan.getPlazo())
                                                                                    .email(loan.getEmail())
                                                                                    .idEstado(LoanState.PENDIENTE.getId())
                                                                                    .idTipoPrestamo(loan.getIdTipoPrestamo())
                                                                                    .build();

                                                                            return loanRepository.save(loanFinal);
                                                                        }
                                                                )

                                                )
                                )

                );
    }

    private String generarCodigoSolicitud() {
        int random = ThreadLocalRandom.current().nextInt(0, 1_0000_0000);
        String padded = String.format("%08d", random);
        int year = LocalDate.now().getYear();
        return year + "-" + padded; // ejemplo: "2025-00001234"
    }


}
