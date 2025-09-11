package co.com.powerup2025.model.user.gateways;

import co.com.powerup2025.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {

     Mono<User> getUserByEmail(String email);
}
