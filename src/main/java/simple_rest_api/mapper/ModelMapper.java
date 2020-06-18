package simple_rest_api.mapper;

import simple_rest_api.model.User;
import simple_rest_api.model.dto.CreateUserDto;
import simple_rest_api.model.dto.UserDto;

public interface ModelMapper {

    static User fromCreateUserDtoToUser(CreateUserDto createUserDto) {
        return User.builder()
                .name(createUserDto.getName())
                .surname(createUserDto.getSurname())
                .username(createUserDto.getUsername())
                .email(createUserDto.getEmail())
                .password(createUserDto.getPassword())
                .role(createUserDto.getRole())
                .enabled(createUserDto.getEnabled())
                .build();
    }

    static UserDto fromUserToUserDto(User user) {
        return UserDto
                .builder()
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .build();
    }
}
