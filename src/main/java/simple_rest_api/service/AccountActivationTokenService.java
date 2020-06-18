package simple_rest_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import simple_rest_api.exceptions.AppException;
import simple_rest_api.model.Token;
import simple_rest_api.repository.TokenRepository;
import simple_rest_api.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountActivationTokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Value("${tokens.mail.expiration.time.minutes}")
    private Integer tokenValidationTimeInMinutes;

    public Token prepareToken(Long userId) {
        if (Objects.isNull(userId)) {
            throw new AppException("ACCOUNT ACTIVATION TOKEN SERVICE: prepareToken() - user id is null");
        }

        var user = userRepository
                .findById(userId)
                .orElseThrow(() -> new AppException("ACCOUNT ACTIVATION TOKEN SERVICE: prepareToken() - no user with id"));

        var token = Token
                .builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expirationDateTime(LocalDateTime.now().plusMinutes(tokenValidationTimeInMinutes))
                .build();

        return tokenRepository.save(token);
    }

    public Long validateToken(String token) {
        if (Objects.isNull(token)) {
            throw new AppException("ACCOUNT ACTIVATION TOKEN SERVICE: validateToken() - token is null");
        }

        var tokenWithUserId = tokenRepository
                .findByToken(token)
                .orElseThrow(() -> new AppException("ACCOUNT ACTIVATION TOKEN SERVICE: validateToken() - token is incorrect"));

        if (tokenWithUserId.getExpirationDateTime().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(tokenWithUserId);
            throw new AppException("ACCOUNT ACTIVATION TOKEN SERVICE: validateToken() - token has expired");
        }

        return tokenWithUserId.getUser().getId();
    }

    public void deleteToken(String token) {
        if (Objects.isNull(token)) {
            throw new AppException("ACCOUNT ACTIVATION TOKEN SERVICE: deleteToken() - token is null");
        }

        var tokenWithUserId = tokenRepository
                .findByToken(token)
                .orElseThrow(() -> new AppException("ACCOUNT ACTIVATION TOKEN SERVICE: deleteToken() - token is incorrect"));

        tokenRepository.delete(tokenWithUserId);
    }

    public Long activateUser(String token) {
        if (Objects.isNull(token)) {
            throw new AppException("ACCOUNT ACTIVATION TOKEN SERVICE: activateUser() - token is null");
        }

        var userId = validateToken(token);
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException("ACCOUNT ACTIVATION TOKEN SERVICE: activateUser() - error when finding user by id"));
        user.setEnabled(true);
        System.out.println("User " + user.getUsername() + " has been activated");
        deleteToken(token);
        System.out.println("Activation token for " + user.getEmail() + " has been removed");
        return userRepository
                .save(user)
                .getId();
    }
}

