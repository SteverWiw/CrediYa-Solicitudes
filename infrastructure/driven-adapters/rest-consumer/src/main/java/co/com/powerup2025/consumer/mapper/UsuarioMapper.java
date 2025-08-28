package co.com.powerup2025.consumer.mapper;

import co.com.powerup2025.consumer.api.model.UsuarioResponseDTO;
import co.com.powerup2025.model.usuario.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    Usuario toModel (UsuarioResponseDTO dto);
}
