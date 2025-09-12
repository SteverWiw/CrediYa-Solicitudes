package co.com.powerup2025.usecase.loan;

import co.com.powerup2025.model.IJWTUtil;
import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.loans.Loan;
import co.com.powerup2025.model.loans.enums.LoanState;
import co.com.powerup2025.model.loans.gateways.LoanRepository;
import co.com.powerup2025.model.loans.gateways.ILoanUseCase;
import co.com.powerup2025.model.loantype.gateways.LoanTypeRepository;
import co.com.powerup2025.model.logger.LoggerFactoryPort;
import co.com.powerup2025.model.logger.LoggerRepository;
import co.com.powerup2025.model.user.gateways.UserRepository;
import co.com.powerup2025.usecase.loan.validations.LoanValidator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.MultiDocPrintService;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;


public class LoanUseCase implements ILoanUseCase {

    private final UserRepository usuarioRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final LoanRepository loanRepository;
    private final LoggerRepository logger;
    private final IJWTUtil  jwtUtil;


    public LoanUseCase(UserRepository usuarioRepository, LoanTypeRepository loanTypeRepository, LoanRepository loanRepository, LoggerFactoryPort logger, IJWTUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.loanTypeRepository = loanTypeRepository;
        this.loanRepository = loanRepository;
        this.logger = logger.getLogger(LoanUseCase.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Loan> createLoan(Loan loan, String token) {
        System.out.println(token);
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
                                                                .flatMap(user -> user.getEmail().equals(jwtUtil.getUsername(token))?
                                                                         Mono.just(user)
                                                                        :Mono.error(new BusinessException(ErrorCode.VAL_014))        )
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

    @Override
    public Flux<Loan> listarSolicitudes(String estado) {
        return loanRepository.obtenerPorEstado(estado);
    }


    private String generarCodigoSolicitud() {
        int random = ThreadLocalRandom.current().nextInt(0, 1_0000_0000);
        String padded = String.format("%08d", random);
        int year = LocalDate.now().getYear();
        return year + "-" + padded;
    }


}
