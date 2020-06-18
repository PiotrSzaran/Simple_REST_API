package simple_rest_api.service;

import lombok.RequiredArgsConstructor;
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

    private final UserRepository userRepository;
    private final CreateUserValidator createUserValidator;
    private final PasswordEncoder passwordEncoder;

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


        return insertUser.getId();
    }
}
