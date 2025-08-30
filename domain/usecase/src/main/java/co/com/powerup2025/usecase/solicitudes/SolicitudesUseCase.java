package co.com.powerup2025.usecase.solicitudes;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.exception.gateways.LoggerFactoryPort;
import co.com.powerup2025.model.exception.gateways.LoggerPort;
import co.com.powerup2025.model.solicitudes.Solicitudes;
import co.com.powerup2025.model.solicitudes.enums.EstadoSolicitud;
import co.com.powerup2025.model.solicitudes.gateways.SolicitudesRepository;
import co.com.powerup2025.model.solicitudes.gateways.SolicitudesService;
import co.com.powerup2025.model.tipoprestamo.gateways.TipoPrestamosRepository;
import co.com.powerup2025.model.usuario.gateways.UsuarioRepository;
import co.com.powerup2025.usecase.solicitudes.validations.SolicitudValidator;

import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;


public class SolicitudesUseCase implements SolicitudesService {

    private final UsuarioRepository usuarioRepository;
    private final TipoPrestamosRepository tipoPrestamosRepository;
    private final SolicitudesRepository solicitudesRepository;
    private final LoggerPort logger;

    public SolicitudesUseCase(UsuarioRepository usuarioRepository, TipoPrestamosRepository tipoPrestamosRepository, SolicitudesRepository solicitudesRepository, LoggerFactoryPort logger) {
        this.usuarioRepository = usuarioRepository;
        this.tipoPrestamosRepository = tipoPrestamosRepository;
        this.solicitudesRepository = solicitudesRepository;
        this.logger = logger.getLogger(SolicitudesService.class);
    }

    @Override
    public Mono<Solicitudes> createLoan(Solicitudes solicitudes) {

        return SolicitudValidator.validar(solicitudes)
                .flatMap(s -> logger.info("Validando datos de solicitud").thenReturn(s))
                .flatMap(validated ->
                        tipoPrestamosRepository.findById(validated.getIdTipoPrestamo())
                                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.VAL_013)))
                                .flatMap(tipoPrestamo ->
                                        SolicitudValidator.validarMontoYPlazo(solicitudes, tipoPrestamo)
                                                .flatMap(sl -> logger.info("Validando datos de prestamo").thenReturn(sl))
                                                .flatMap(s ->
                                                        usuarioRepository.getUserByEmail(solicitudes.getEmail())
                                                                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.USR_001)))
                                                                .flatMap(usuario -> {
                                                                            String codigoSolicitud = generarCodigoSolicitud();

                                                                            Solicitudes solicitudesFinal = solicitudes.toBuilder()
                                                                                    .codigoSolicitud(codigoSolicitud)
                                                                                    .monto(solicitudes.getMonto())
                                                                                    .plazo(solicitudes.getPlazo())
                                                                                    .email(solicitudes.getEmail())
                                                                                    .idEstado(EstadoSolicitud.PENDIENTE.getId())
                                                                                    .idTipoPrestamo(solicitudes.getIdTipoPrestamo())
                                                                                    .build();

                                                                            return solicitudesRepository.save(solicitudesFinal);
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
