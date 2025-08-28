package co.com.powerup2025.model.usuario.gateways;

import co.com.powerup2025.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {

    public Mono<Usuario> getUserByEmail(String email);
}
