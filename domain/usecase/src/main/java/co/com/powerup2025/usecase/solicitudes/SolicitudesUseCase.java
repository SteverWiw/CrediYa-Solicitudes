package co.com.powerup2025.usecase.solicitudes;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.solicitudes.Solicitudes;
import co.com.powerup2025.model.solicitudes.enums.EstadoSolicitud;
import co.com.powerup2025.model.solicitudes.gateways.SolicitudesRepository;
import co.com.powerup2025.model.solicitudes.gateways.SolicitudesService;
import co.com.powerup2025.model.tipoprestamo.gateways.TipoPrestamosRepository;
import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.model.usuario.gateways.UsuarioRepository;
import co.com.powerup2025.usecase.solicitudes.validations.SolicitudValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudesUseCase implements SolicitudesService {

    private final UsuarioRepository usuarioRepository;
    private final TipoPrestamosRepository tipoPrestamosRepository;
    private final SolicitudesRepository solicitudesRepository;

    @Override
    public Mono<Solicitudes> createLoan(Solicitudes solicitudes) {
        return SolicitudValidator.validar(solicitudes)
                .flatMap(validated ->
                        usuarioRepository.getUserByEmail(solicitudes.getEmail())
                                .flatMap(usuario ->
                                        tipoPrestamosRepository.findById(solicitudes.getIdTipoPrestamo())
                                                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.VAL_013)))
                                                .flatMap(tipoPrestamo -> {
                                                    Solicitudes solicitudesFinal = solicitudes.toBuilder()
                                                            .monto(solicitudes.getMonto())
                                                            .plazo(solicitudes.getPlazo())
                                                            .email(solicitudes.getEmail())
                                                            .idEstado(EstadoSolicitud.PENDIENTE.getId())
                                                            .idTipoPrestamo(solicitudes.getIdTipoPrestamo())
                                                            .build();

                                                    return solicitudesRepository.save(solicitudesFinal);
                                                })
                                )
                );
    }


}
