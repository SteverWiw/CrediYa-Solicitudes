package co.com.powerup2025.usecase.loan.validations;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.loans.Loan;
import co.com.powerup2025.model.loantype.LoanType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class LoanValidator {

    private static final Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public static Mono<Loan> validar(Loan loan) {
        return Flux.just(
                        Tuples.of(isBlank(loan.getEmail()), ErrorCode.VAL_003),
                        Tuples.of(!emailPattern.matcher(loan.getEmail()).matches(),
                                ErrorCode.VAL_009),
                        Tuples.of(isBlank(String.valueOf(loan.getMonto())), ErrorCode.VAL_010),
                        Tuples.of(loan.getIdTipoPrestamo() == null, ErrorCode.VAL_011),
                        Tuples.of(isBlank(String.valueOf(loan.getPlazo())), ErrorCode.VAL_012)
                ).filter(Tuple2::getT1)
                .map(Tuple2::getT2)
                .collectList()
                .flatMap(errorCodes -> errorCodes.isEmpty()
                        ? Mono.just(loan)
                        : Mono.error(new BusinessException(errorCodes)));

    }

    public static Mono<Loan> validarMontoYPlazo(Loan solicitud, LoanType loanType) {
        BigDecimal monto = solicitud.getMonto();
        Integer plazo = solicitud.getPlazo();

        return Flux.just(
                        Tuples.of(monto.compareTo(loanType.getMontoMinimo()) < 0, ErrorCode.SOL_002),
                        Tuples.of(monto.compareTo(loanType.getMontoMaximo()) > 0, ErrorCode.SOL_003),
                        Tuples.of(plazo < loanType.getPlazoMinimo(), ErrorCode.SOL_004),
                        Tuples.of(plazo > loanType.getPlazoMaximo(), ErrorCode.SOL_005)
                )
                .filter(Tuple2::getT1)
                .map(Tuple2::getT2)
                .collectList()
                .flatMap(errorCodes -> errorCodes.isEmpty()
                        ? Mono.just(solicitud)
                        : Mono.error(new BusinessException(errorCodes)));
    }



    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }


}
