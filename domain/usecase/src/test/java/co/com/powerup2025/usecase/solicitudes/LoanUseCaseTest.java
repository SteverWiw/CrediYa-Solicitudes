package co.com.powerup2025.usecase.solicitudes;

import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.loans.Loan;
import co.com.powerup2025.model.loans.enums.LoanState;
import co.com.powerup2025.model.loans.gateways.LoanRepository;
import co.com.powerup2025.model.loantype.LoanType;
import co.com.powerup2025.model.loantype.gateways.LoanTypeRepository;
import co.com.powerup2025.model.user.User;
import co.com.powerup2025.model.user.gateways.UserRepository;
import co.com.powerup2025.usecase.loan.LoanUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.mockito.ArgumentCaptor;
import reactor.util.context.Context;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanUseCaseTest {

    @Mock
    private UserRepository usuarioRepository;

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoggerFactoryPort loggerFactory;

    @Mock
    private LoggerPort logger;

    private LoanUseCase useCase;

    private User usuarioMock;

    @BeforeEach
    void setUp() {
        when(loggerFactory.getLogger(LoanUseCase.class)).thenReturn(logger);
        lenient().when(logger.info(anyString())).thenReturn(Mono.empty());

        useCase = new LoanUseCase(
                usuarioRepository,
                loanTypeRepository,
                loanRepository,
                loggerFactory
        );

        usuarioMock = User.builder()
                .idUsuario(1)
                .nombre("Test")
                .apellido("User")
                .email("test@mail.com")
                .documentoIdentidad(123456789L)
                .telefono("3001234567")
                .idRol(2)
                .salarioBase(BigDecimal.valueOf(5000))
                .build();
    }

    @Test
    void createLoan_Success_WithTraceIdValidation() {
        Loan solicitud = Loan.builder()
                .email(usuarioMock.getEmail())
                .monto(BigDecimal.valueOf(1000.0))
                .plazo(12)
                .idTipoPrestamo(1)
                .build();

        LoanType loanType = LoanType.builder()
                .montoMinimo(BigDecimal.valueOf(500))
                .montoMaximo(BigDecimal.valueOf(2000))
                .plazoMinimo(6)
                .plazoMaximo(24)
                .build();

        ArgumentCaptor<String> mensajeCaptor = ArgumentCaptor.forClass(String.class);

        when(logger.info(mensajeCaptor.capture())).thenAnswer(invocation -> {
            return Mono.deferContextual(ctx -> {
                assertTrue(ctx.hasKey("traceId"), "traceId no está presente en el Reactor Context");
                String traceId = ctx.get("traceId");
                assertNotNull(traceId);
                assertFalse(traceId.isBlank());
                return Mono.empty();
            });
        });

        when(loanTypeRepository.findById(1)).thenReturn(Mono.just(loanType));
        when(usuarioRepository.getUserByEmail(usuarioMock.getEmail())).thenReturn(Mono.just(usuarioMock));
        when(loanRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(
                        useCase.createLoan(solicitud)
                                .contextWrite(Context.of("traceId", "abc123-trace"))
                )
                .assertNext(saved -> {
                    assertEquals(usuarioMock.getEmail(), saved.getEmail());
                    assertEquals(LoanState.PENDIENTE.getId(), saved.getIdEstado());
                    assertNotNull(saved.getCodigoSolicitud());
                    assertEquals(solicitud.getMonto(), saved.getMonto());
                })
                .verifyComplete();

        verify(logger, atLeastOnce()).info(anyString());
    }

    @Test
    void createLoan_ValidationFails() {
        Loan solicitud = Loan.builder()
                .email("correo-invalido") // formato incorrecto
                .monto(null)
                .idTipoPrestamo(1)
                .plazo(null)
                .build();

        StepVerifier.create(useCase.createLoan(solicitud))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void createLoan_TipoPrestamoNotFound() {
        Loan solicitud = Loan.builder()
                .email(usuarioMock.getEmail())
                .monto(BigDecimal.valueOf(1000))
                .plazo(12)
                .idTipoPrestamo(1)
                .build();

        when(loanTypeRepository.findById(1)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.createLoan(solicitud))
                .expectError(BusinessException.class)
                .verify();

    }

    @Test
    void generarCodigoSolicitud_Format() throws Exception {
        Method method = LoanUseCase.class.getDeclaredMethod("generarCodigoSolicitud");
        method.setAccessible(true);
        String codigo = (String) method.invoke(useCase);

        assertTrue(codigo.matches("\\d{4}-\\d{8}"));
        assertEquals(LocalDate.now().getYear(), Integer.parseInt(codigo.substring(0, 4)));
    }
}
