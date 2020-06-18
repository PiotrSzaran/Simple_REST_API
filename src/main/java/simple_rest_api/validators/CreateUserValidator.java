package simple_rest_api.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import simple_rest_api.exceptions.AppException;
import simple_rest_api.model.dto.CreateUserDto;
import simple_rest_api.repository.UserRepository;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CreateUserValidator extends AbstractValidator<CreateUserDto> {
    private final UserRepository userRepository;

    @Override
    public Map<String, String> validate(CreateUserDto createUserDto) {
        errors.clear();

        if (Objects.isNull(createUserDto)) {
            errors.put("create player", "object is null");
        }

        if (!isNameSurnameValid(createUserDto.getName())) {
            errors.put("name", "name is incorrect");
        }

        if (!isNameSurnameValid(createUserDto.getSurname())) {
            errors.put("surname", "surname is incorrect");
        }

        if (!isUsernameValid(createUserDto.getUsername())) {
            errors.put("username", "username is incorrect");
        }

        if (doesUserExists(createUserDto.getUsername())) {
            errors.put("username", "Username already taken");
        }

        if (!isEmailValid(createUserDto.getEmail())) {
            errors.put("email", "email is incorrect");
        }

        if (doesEmailExists(createUserDto.getEmail())) {
            errors.put("email", "Email is already registered");
        }

        if (!isPasswordValid(createUserDto.getPassword())) {
            errors.put("password", "Password is too short! Should be at least 6 chars");
        }

        if (!createUserDto.getPassword().equals(createUserDto.getPasswordConfirmation())) {
            errors.put("password", "The given passwords are different");
        }

        if (Objects.isNull(createUserDto.getRole())) {
            errors.put("role", "role is empty");
        }

        return errors;
    }

    private boolean doesUserExists(String username) {
        if (Objects.isNull(username)) {
            throw new AppException("username is null: ");
        }
        return userRepository.findByUsername(username).isPresent();
    }

    private boolean doesEmailExists(String emailAddress) {
        if (Objects.isNull(emailAddress)) {
            throw new AppException("email address is null:");
        }
        return userRepository.findByEmail(emailAddress).isPresent();
    }

    //the same for name and surname
    private boolean isNameSurnameValid(String name) {
        return Objects.nonNull(name) && name.matches("[A-Za-z]+");
    }

    private boolean isUsernameValid(String username) {
        return Objects.nonNull(username) && username.matches("[A-Za-z\\d]+");
    }

    private boolean isEmailValid(String email) {
        //REGEXP from https://stackoverflow.com/questions/201323/how-to-validate-an-email-address-using-a-regular-expression
        return Objects.nonNull(email) && email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    }

    //should be at least 6 chars long
    private boolean isPasswordValid(String password) {
        return Objects.nonNull(password) && password.length() > 5;
    }
}
