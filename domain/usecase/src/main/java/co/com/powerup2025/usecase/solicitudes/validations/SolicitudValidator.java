package co.com.powerup2025.usecase.solicitudes.validations;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.solicitudes.Solicitudes;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.regex.Pattern;

public class SolicitudValidator {
    private SolicitudValidator() {
        throw new IllegalStateException("Utiliti class");
    }

    private static final Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public static Mono<Solicitudes> validar(Solicitudes solicitudes) {
        return Flux.just(
                        Tuples.of(isBlank(solicitudes.getEmail()), ErrorCode.VAL_003),
                        Tuples.of(isBlank(String.valueOf(solicitudes.getMonto())), ErrorCode.VAL_010),
                        Tuples.of(solicitudes.getIdTipoPrestamo() == null, ErrorCode.VAL_011),
                        Tuples.of(isBlank(String.valueOf(solicitudes.getPlazo())), ErrorCode.VAL_012)
                ).filter(Tuple2::getT1)
                .map(Tuple2::getT2)
                .collectList()
                .flatMap(errorCodes -> errorCodes.isEmpty()
                        ? Mono.just(solicitudes)
                        : Mono.error(new BusinessException(errorCodes)));

    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }


}
