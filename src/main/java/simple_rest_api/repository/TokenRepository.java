package simple_rest_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import simple_rest_api.model.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);
}
