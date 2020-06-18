package simple_rest_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import simple_rest_api.exceptions.AppException;
import simple_rest_api.mapper.ModelMapper;
import simple_rest_api.model.dto.CreateUserDto;
import simple_rest_api.repository.UserRepository;
import simple_rest_api.validators.CreateUserValidator;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRegistrationService {

    @Value("${server.port}")
    private String port;

    private final UserRepository userRepository;
    private final CreateUserValidator createUserValidator;
    private final PasswordEncoder passwordEncoder;
    private final AccountActivationTokenService accountActivationTokenService;
    private final EmailService emailService;

    public Long create(CreateUserDto createUserDto) {
        if (Objects.isNull(createUserDto)) {
            throw new AppException("createUserDto is null");
        }
        var errors = createUserValidator.validate(createUserDto);

        var errorMessage = errors
                .entrySet()
                .stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining(", "));

        if (createUserValidator.hasErrors()) {
            throw new AppException(errorMessage);
        }

        var user = ModelMapper.fromCreateUserDtoToUser(createUserDto);
        user.setEnabled(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var insertUser = userRepository.save(user);
        System.out.println("User " + user.getUsername() + " has been added");

        // -------------------------
        // EMAIL CREATION
        // -------------------------
        var tokenWithUser = accountActivationTokenService.prepareToken(insertUser.getId());
        System.out.println("Account activation token for address " + user.getEmail() + " has been generated");

        var messagePart1 = "Click link to activate account: ";
        var messagePart2 = "http://localhost:" + port +"/security/account-activation?token=";
        var messagePart3 = tokenWithUser.getToken();
        var message = messagePart1 + messagePart2 + messagePart3;

        // -------------------------
        // EMAIL DELIVERY
        // -------------------------
        emailService.send(insertUser.getEmail(), "Account Activation - Advertisement Platform", message);
        System.out.println("Account activation token for address " + insertUser.getEmail() + " has been sent");

        return insertUser.getId();
    }
}
