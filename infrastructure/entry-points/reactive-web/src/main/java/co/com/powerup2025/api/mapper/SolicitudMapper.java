package co.com.powerup2025.api.mapper;

import co.com.powerup2025.api.request_dto.SolicitudRequestDTO;
import co.com.powerup2025.api.response_dto.SolicitudResponseDTO;
import co.com.powerup2025.model.solicitudes.Solicitudes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface SolicitudMapper {

    SolicitudMapper INSTANCE = getMapper(SolicitudMapper.class);

    // --- Request DTO → Entity ---
    @Mapping(target = "monto",source = "monto")
    @Mapping(target = "plazo",source = "plazo")
    @Mapping(target = "email",source = "email")
    @Mapping(target = "idTipoPrestamo",source = "idTipoPrestamo")
    @Mapping(target = "idSolicitud",ignore = true)
    @Mapping(target = "fechaCreacion",ignore = true)
    @Mapping(target = "codigoSolicitud",ignore = true)
    @Mapping(target = "idEstado",ignore = true)//
    Solicitudes toEntity(SolicitudRequestDTO dto);




    // --- Entity → Response DTO ---
    @Mapping(target = "solicitudId", source = "codigoSolicitud")
    @Mapping(target = "estado", expression = "java(co.com.powerup2025.model.solicitudes.enums.EstadoSolicitud.getEstado(solicitudes.getIdEstado()))")
    @Mapping(target = "mensaje", expression = "java(co.com.powerup2025.model.solicitudes.enums.EstadoSolicitud.getMessage(solicitudes.getIdEstado()))")//
    SolicitudResponseDTO toDto(Solicitudes solicitudes);

    default String mapCodigoSolicitud(String codigo) {
        return codigo != null ? codigo : "";
    }

}
