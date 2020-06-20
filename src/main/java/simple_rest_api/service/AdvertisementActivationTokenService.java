package simple_rest_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import simple_rest_api.exceptions.AppException;
import simple_rest_api.model.Token;
import simple_rest_api.repository.AdvertisementRepository;
import simple_rest_api.repository.TokenRepository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdvertisementActivationTokenService {

    private final TokenRepository tokenRepository;
    private final AdvertisementRepository advertisementRepository;

    @Value("${tokens.mail.expiration.time.minutes}")
    private Integer tokenValidationTimeInMinutes;

    public Token prepareToken(Long advertisementId) {
        if (Objects.isNull(advertisementId)) {
            throw new AppException("ADVERTISEMENT ACTIVATION TOKEN SERVICE: prepareToken() - advertisement id is null");
        }

        var advertisement = advertisementRepository
                .findById(advertisementId)
                .orElseThrow(() -> new AppException("ADVERTISEMENT ACTIVATION TOKEN SERVICE: prepareToken() - no advertisement with id"));

        var token = Token
                .builder()
                .token(UUID.randomUUID().toString())
                .advertisement(advertisement)
                .expirationDateTime(LocalDateTime.now().plusMinutes(tokenValidationTimeInMinutes))
                .build();

        return tokenRepository.save(token);
    }

    public Long validateToken(String token) {
        if (Objects.isNull(token)) {
            throw new AppException("ADVERTISEMENT ACTIVATION TOKEN SERVICE: validateToken() - token is null");
        }

        var tokenWithAdvertisementId = tokenRepository
                .findByToken(token)
                .orElseThrow(() -> new AppException("ADVERTISEMENT ACTIVATION TOKEN SERVICE: validateToken() - token is incorrect"));

        if (tokenWithAdvertisementId.getExpirationDateTime().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(tokenWithAdvertisementId);
            throw new AppException("ADVERTISEMENT ACTIVATION TOKEN SERVICE: validateToken() - token has expired");
        }

        return tokenWithAdvertisementId.getAdvertisement().getId();
    }

    public void deleteToken(String token) {
        if (Objects.isNull(token)) {
            throw new AppException("ADVERTISEMENT ACTIVATION TOKEN SERVICE: deleteToken() - token is null");
        }

        var tokenFromDB = tokenRepository
                .findByToken(token)
                .orElseThrow(() -> new AppException("ADVERTISEMENT ACTIVATION TOKEN SERVICE: deleteToken() - token is incorrect"));

        tokenRepository.delete(tokenFromDB);
    }

    public Long activateAdvertisement(String token) {
        if (Objects.isNull(token)) {
            throw new AppException("ADVERTISEMENT ACTIVATION TOKEN SERVICE: activateAdvertisement() - token is null");
        }

        var advertisementId = validateToken(token);
        var advertisement = advertisementRepository.findById(advertisementId).orElseThrow(
                () -> new AppException("ADVERTISEMENT ACTIVATION TOKEN SERVICE: activateUser() - error when finding advertisement by id"));
        advertisement.setIsActivated(true);
        System.out.println("Advertisement " + advertisement.getTitle() + " has been activated");
        deleteToken(token);
        System.out.println("Activation token for " + advertisement.getTitle() + " has been removed");
        return advertisementRepository
                .save(advertisement)
                .getId();
    }
}
