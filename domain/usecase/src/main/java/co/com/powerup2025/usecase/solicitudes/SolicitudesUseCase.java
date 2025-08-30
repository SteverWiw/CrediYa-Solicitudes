package co.com.powerup2025.usecase.solicitudes;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import co.com.powerup2025.model.exception.gateways.LoggerPort;
import co.com.powerup2025.model.solicitudes.Solicitudes;
import co.com.powerup2025.model.solicitudes.gateways.SolicitudesRepository;
import co.com.powerup2025.model.solicitudes.gateways.SolicitudesService;
import co.com.powerup2025.model.tipoprestamo.gateways.TipoPrestamosRepository;
import co.com.powerup2025.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudesUseCase implements SolicitudesService {

        private final UsuarioRepository usuarioRepository;
        private final TipoPrestamosRepository tipoPrestamosRepository;
        private final SolicitudesRepository solicitudesRepository;
        private final LoggerPort logger;

    @Override
    public Mono<Solicitudes> createLoan(Solicitudes solicitudes) {
        return SolicitudValidator.validar(solicitudes)
                .flatMap(validated ->
                
                        usuarioRepository.getUserByEmail(solicitudes.getEmail())
                                
                                                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.VAL_013)))
                                                .flatMap(tipoPrestamo ->
                                                        SolicitudValidator.validarMontoYPlazo(solicitudes, tipoPrestamo)
                                                                .flatMap(validado -> {
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
                                                                })

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
