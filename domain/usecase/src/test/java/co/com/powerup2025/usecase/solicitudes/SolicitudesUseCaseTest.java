package co.com.powerup2025.usecase.solicitudes;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.exception.gateways.LoggerFactoryPort;
import co.com.powerup2025.model.exception.gateways.LoggerPort;
import co.com.powerup2025.model.solicitudes.Solicitudes;
import co.com.powerup2025.model.solicitudes.enums.EstadoSolicitud;
import co.com.powerup2025.model.solicitudes.gateways.SolicitudesRepository;
import co.com.powerup2025.model.tipoprestamo.TipoPrestamo;
import co.com.powerup2025.model.tipoprestamo.gateways.TipoPrestamosRepository;
import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.model.usuario.gateways.UsuarioRepository;
import co.com.powerup2025.usecase.solicitudes.validations.SolicitudValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.mockito.ArgumentCaptor;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudesUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoPrestamosRepository tipoPrestamosRepository;

    @Mock
    private SolicitudesRepository solicitudesRepository;

    @Mock
    private LoggerFactoryPort loggerFactory;

    @Mock
    private LoggerPort logger;

    private SolicitudesUseCase useCase;

    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        when(loggerFactory.getLogger(SolicitudesUseCase.class)).thenReturn(logger);
        lenient().when(logger.info(anyString())).thenReturn(Mono.empty());

        useCase = new SolicitudesUseCase(
                usuarioRepository,
                tipoPrestamosRepository,
                solicitudesRepository,
                loggerFactory
        );


        usuarioMock = Usuario.builder()
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
        Solicitudes solicitud = Solicitudes.builder()
                .email(usuarioMock.getEmail())
                .monto(BigDecimal.valueOf(1000.0))
                .plazo(12)
                .idTipoPrestamo(1)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
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

        when(tipoPrestamosRepository.findById(1)).thenReturn(Mono.just(tipoPrestamo));
        when(usuarioRepository.getUserByEmail(usuarioMock.getEmail())).thenReturn(Mono.just(usuarioMock));
        when(solicitudesRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        // Act & Assert
        StepVerifier.create(
                        useCase.createLoan(solicitud)
                                .contextWrite(Context.of("traceId", "abc123-trace"))
                )
                .assertNext(saved -> {
                    assertEquals(usuarioMock.getEmail(), saved.getEmail());
                    assertEquals(EstadoSolicitud.PENDIENTE.getId(), saved.getIdEstado());
                    assertNotNull(saved.getCodigoSolicitud());
                    assertEquals(solicitud.getMonto(), saved.getMonto());
                })
                .verifyComplete();

        verify(logger, atLeastOnce()).info(anyString());
    }


    @Test
    void createLoan_ValidationFails() {
        Solicitudes solicitud = Solicitudes.builder()
                .email("invalid-email")
                .idTipoPrestamo(1)
                .build();

        try (MockedStatic<SolicitudValidator> validatorMock = mockStatic(SolicitudValidator.class)) {
            validatorMock.when(() -> SolicitudValidator.validar(solicitud))
                    .thenReturn(Mono.error(new BusinessException(ErrorCode.VAL_009)));

            StepVerifier.create(useCase.createLoan(solicitud))
                    .expectErrorMatches(BusinessException.class::isInstance)
                    .verify();
        }
    }

    @Test
    void createLoan_TipoPrestamoNotFound() {
        Solicitudes solicitud = Solicitudes.builder()
                .email(usuarioMock.getEmail())
                .idTipoPrestamo(1)
                .build();

        try (MockedStatic<SolicitudValidator> validatorMock = mockStatic(SolicitudValidator.class)) {
            validatorMock.when(() -> SolicitudValidator.validar(solicitud)).thenReturn(Mono.just(solicitud));

            when(tipoPrestamosRepository.findById(1)).thenReturn(Mono.empty());

            StepVerifier.create(useCase.createLoan(solicitud))
                    .expectErrorMatches(BusinessException.class::isInstance )
                    .verify();
        }
    }

    @Test
    void createLoan_UsuarioNotFound() {
        Solicitudes solicitud = Solicitudes.builder()
                .email(usuarioMock.getEmail())
                .idTipoPrestamo(1)
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .montoMinimo(BigDecimal.valueOf(500))
                .montoMaximo(BigDecimal.valueOf(2000))
                .plazoMinimo(6)
                .plazoMaximo(24)
                .build();

        try (MockedStatic<SolicitudValidator> validatorMock = mockStatic(SolicitudValidator.class)) {
            validatorMock.when(() -> SolicitudValidator.validar(solicitud)).thenReturn(Mono.just(solicitud));
            validatorMock.when(() -> SolicitudValidator.validarMontoYPlazo(solicitud, tipoPrestamo)).thenReturn(Mono.just(solicitud));

            when(tipoPrestamosRepository.findById(1)).thenReturn(Mono.just(tipoPrestamo));
            when(usuarioRepository.getUserByEmail(usuarioMock.getEmail())).thenReturn(Mono.empty());

            StepVerifier.create(useCase.createLoan(solicitud))
                    .expectErrorMatches(BusinessException.class::isInstance )
                    .verify();
        }
    }

    @Test
    void generarCodigoSolicitud_Format() throws Exception {
        Method method = SolicitudesUseCase.class.getDeclaredMethod("generarCodigoSolicitud");
        method.setAccessible(true);
        String codigo = (String) method.invoke(useCase);

        assertTrue(codigo.matches("\\d{4}-\\d{8}"));
        assertEquals(LocalDate.now().getYear(), Integer.parseInt(codigo.substring(0, 4)));
    }
}
