package co.com.powerup2025.api.mapper;

import co.com.powerup2025.api.dtos.request.LoanRequest;
import co.com.powerup2025.api.dtos.response.LoanResponse;
import co.com.powerup2025.model.loans.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    LoanMapper INSTANCE = getMapper(LoanMapper.class);

    // --- Request DTO → Entity ---
    @Mapping(target = "monto",source = "monto")
    @Mapping(target = "plazo",source = "plazo")
    @Mapping(target = "email",source = "email")
    @Mapping(target = "idTipoPrestamo",source = "idTipoPrestamo")
    @Mapping(target = "idSolicitud",ignore = true)
    @Mapping(target = "fechaCreacion",ignore = true)
    @Mapping(target = "codigoSolicitud",ignore = true)
    @Mapping(target = "idEstado",ignore = true)//
    Loan toEntity(LoanRequest dto);




    // --- Entity → Response DTO ---
    @Mapping(target = "solicitudId", source = "codigoSolicitud")
    @Mapping(target = "estado", expression = "java(co.com.powerup2025.model.loans.enums.LoanState.getEstado(loan.getIdEstado()))")
    @Mapping(target = "mensaje", expression = "java(co.com.powerup2025.model.loans.enums.LoanState.getMessage(loan.getIdEstado()))")//
    LoanResponse toDto(Loan loan);

    default String mapCodigoSolicitud(String codigo) {
        return codigo != null ? codigo : "";
    }

}
