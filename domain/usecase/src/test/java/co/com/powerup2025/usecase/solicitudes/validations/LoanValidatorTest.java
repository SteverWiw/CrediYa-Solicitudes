package co.com.powerup2025.usecase.solicitudes.validations;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.exception.gateways.IErrorCode;
import co.com.powerup2025.model.loans.Loan;
import co.com.powerup2025.model.loantype.LoanType;
import co.com.powerup2025.usecase.loan.validations.LoanValidator;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanValidatorTest {

    @Test
    void validarSolicitudConErroresMultiples() {
        Loan solicitud = Loan.builder()
                .email("")
                .monto(null)
                .idTipoPrestamo(null)
                .plazo(null)
                .build();

        StepVerifier.create(LoanValidator.validar(solicitud))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof BusinessException);
                    BusinessException ex = (BusinessException) error;

                    List<IErrorCode> codes = ex.getErrorCodes();
                    assertEquals(3, codes.size());
                    assertTrue(codes.stream().anyMatch(c -> c.code().equals("VAL_003")));
                    assertTrue(codes.stream().anyMatch(c -> c.code().equals("VAL_009")));
                    assertTrue(codes.stream().anyMatch(c -> c.code().equals("VAL_011")));
                })
                .verify();
    }

    @Test
    void validarSolicitudExitosa() {
        Loan solicitud = Loan.builder()
                .email("usuario@correo.com")
                .monto(BigDecimal.valueOf(1000))
                .idTipoPrestamo(1)
                .plazo(12)
                .build();

        StepVerifier.create(LoanValidator.validar(solicitud))
                .expectNext(solicitud)
                .verifyComplete();
    }

    @Test
    void validarMontoYPlazoFueraDeRango() {
        Loan solicitud = Loan.builder()
                .monto(BigDecimal.valueOf(5000)) // fuera de rango
                .plazo(60)                        // fuera de rango
                .build();

        LoanType tipo = LoanType.builder()
                .montoMinimo(BigDecimal.valueOf(100))
                .montoMaximo(BigDecimal.valueOf(3000))
                .plazoMinimo(6)
                .plazoMaximo(36)
                .build();

        StepVerifier.create(LoanValidator.validarMontoYPlazo(solicitud, tipo))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof BusinessException);
                    BusinessException ex = (BusinessException) error;
                    List<IErrorCode> codes = ex.getErrorCodes();
                    assertTrue(codes.contains(ErrorCode.SOL_003)); // monto > máximo
                    assertTrue(codes.contains(ErrorCode.SOL_005)); // plazo > máximo
                })
                .verify();
    }

    @Test
    void validarMontoYPlazoCorrectos() {
        Loan solicitud = Loan.builder()
                .monto(BigDecimal.valueOf(1500))
                .plazo(12)
                .build();

        LoanType tipo = LoanType.builder()
                .montoMinimo(BigDecimal.valueOf(1000))
                .montoMaximo(BigDecimal.valueOf(2000))
                .plazoMinimo(6)
                .plazoMaximo(24)
                .build();

        StepVerifier.create(LoanValidator.validarMontoYPlazo(solicitud, tipo))
                .expectNext(solicitud)
                .verifyComplete();
    }
}