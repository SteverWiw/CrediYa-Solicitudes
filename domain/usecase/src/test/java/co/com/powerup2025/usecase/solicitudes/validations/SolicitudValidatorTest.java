package co.com.powerup2025.usecase.solicitudes.validations;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.exception.gateways.iErrorCode;
import co.com.powerup2025.model.solicitudes.Solicitudes;
import co.com.powerup2025.model.tipoprestamo.TipoPrestamo;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudValidatorTest {

    @Test
    void validarSolicitudConErroresMultiples() {
        Solicitudes solicitud = Solicitudes.builder()
                .email("")
                .monto(null)
                .idTipoPrestamo(null)
                .plazo(null)
                .build();

        StepVerifier.create(SolicitudValidator.validar(solicitud))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof BusinessException);
                    BusinessException ex = (BusinessException) error;

                    List<iErrorCode> codes = ex.getErrorCodes();
                    assertEquals(3, codes.size());
                    assertTrue(codes.stream().anyMatch(c -> c.code().equals("VAL_003")));
                    assertTrue(codes.stream().anyMatch(c -> c.code().equals("VAL_009")));
                    assertTrue(codes.stream().anyMatch(c -> c.code().equals("VAL_011")));
                })
                .verify();
    }

    @Test
    void validarSolicitudExitosa() {
        Solicitudes solicitud = Solicitudes.builder()
                .email("usuario@correo.com")
                .monto(BigDecimal.valueOf(1000))
                .idTipoPrestamo(1)
                .plazo(12)
                .build();

        StepVerifier.create(SolicitudValidator.validar(solicitud))
                .expectNext(solicitud)
                .verifyComplete();
    }

    @Test
    void validarMontoYPlazoFueraDeRango() {
        Solicitudes solicitud = Solicitudes.builder()
                .monto(BigDecimal.valueOf(5000)) // fuera de rango
                .plazo(60)                        // fuera de rango
                .build();

        TipoPrestamo tipo = TipoPrestamo.builder()
                .montoMinimo(BigDecimal.valueOf(100))
                .montoMaximo(BigDecimal.valueOf(3000))
                .plazoMinimo(6)
                .plazoMaximo(36)
                .build();

        StepVerifier.create(SolicitudValidator.validarMontoYPlazo(solicitud, tipo))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof BusinessException);
                    BusinessException ex = (BusinessException) error;
                    List<iErrorCode> codes = ex.getErrorCodes();
                    assertTrue(codes.contains(ErrorCode.SOL_003)); // monto > máximo
                    assertTrue(codes.contains(ErrorCode.SOL_005)); // plazo > máximo
                })
                .verify();
    }

    @Test
    void validarMontoYPlazoCorrectos() {
        Solicitudes solicitud = Solicitudes.builder()
                .monto(BigDecimal.valueOf(1500))
                .plazo(12)
                .build();

        TipoPrestamo tipo = TipoPrestamo.builder()
                .montoMinimo(BigDecimal.valueOf(1000))
                .montoMaximo(BigDecimal.valueOf(2000))
                .plazoMinimo(6)
                .plazoMaximo(24)
                .build();

        StepVerifier.create(SolicitudValidator.validarMontoYPlazo(solicitud, tipo))
                .expectNext(solicitud)
                .verifyComplete();
    }
}