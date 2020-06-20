package simple_rest_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simple_rest_api.exceptions.AppException;
import simple_rest_api.mapper.ModelMapper;
import simple_rest_api.model.Role;
import simple_rest_api.model.dto.CreateAdvertisementDto;
import simple_rest_api.repository.AdvertisementRepository;
import simple_rest_api.repository.UserRepository;
import simple_rest_api.validators.AdvertisementValidator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateAdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementValidator advertisementValidator;
    private final UserRepository userRepository;
    private final AdvertisementActivationTokenService advertisementActivationTokenService;
    private final EmailService emailService;

    public Long create(CreateAdvertisementDto createAdvertisementDto) {
        if (Objects.isNull(createAdvertisementDto)) {
            throw new AppException("advertisementDto is null");
        }

        var errors = advertisementValidator.validate(createAdvertisementDto);

        var errorMessage = errors
                .entrySet()
                .stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining(", "));

        if (advertisementValidator.hasErrors()) {
            throw new AppException(errorMessage);
        }

        var advertisement = ModelMapper.fromCreateAdvertisementDtoToAdvertisement(createAdvertisementDto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException("cannot get user from DB"));

        advertisement.setUser(user);
        advertisement.setIsActivated(false);
        advertisement.setPublicationDate(LocalDateTime.now());

        if (advertisement.getIsPromoted().equals(true)) {
            if (user.getRole().equals(Role.ROLE_SUBSCRIBED)) {
                advertisement.setPromotionDate(LocalDateTime.now().plusDays(14));
            } else {
                advertisement.setIsPromoted(false);
            }
        }

        var insertedAdvertisement = advertisementRepository.save(advertisement);

        // -------------------------
        // EMAIL CREATION
        // -------------------------
        var tokenWithAdvertisement = advertisementActivationTokenService.prepareToken(insertedAdvertisement.getId());
        System.out.println("Advertisement activation token for address " + user.getEmail() + " has been generated");

        var messagePart1 = "Click link to activate your advertisement: ";
        var messagePart2 = "http://localhost:8095/security/advertisement-activation?token=";
        var messagePart3 = tokenWithAdvertisement.getToken();
        var message = messagePart1 + messagePart2 + messagePart3;

        // -------------------------
        // EMAIL DELIVERY
        // -------------------------
        emailService.send(user.getEmail(), "Advertisement Activation - Advertisement Platform", message);
        System.out.println("Advertisement activation token for address " + user.getEmail() + " has been sent");

        return insertedAdvertisement.getId();
    }
}
