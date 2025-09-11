package co.com.powerup2025.consumer.mapper;

import co.com.powerup2025.consumer.api.model.UserResponseDTO;
import co.com.powerup2025.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toModel (UserResponseDTO dto);
}
