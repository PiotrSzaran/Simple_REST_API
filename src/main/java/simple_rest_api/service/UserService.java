package simple_rest_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import simple_rest_api.exceptions.AppException;
import simple_rest_api.mapper.ModelMapper;
import simple_rest_api.model.dto.UserDto;
import simple_rest_api.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> getAll() {
        return userRepository
                .findAll()
                .stream()
                .map(ModelMapper::fromUserToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getById(Long id) {
        if (Objects.isNull(id)) {
            throw new AppException("id is null");
        }
        return userRepository
                .findById(id)
                .map(ModelMapper::fromUserToUserDto)
                .orElseThrow(() ->new AppException("cannot get user with id: " + id));
    }
}
