package simple_rest_api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simple_rest_api.model.dto.UserDto;
import simple_rest_api.model.dto.response.ResponseData;
import simple_rest_api.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseData<List<UserDto>> getAll() {
        return ResponseData.<List<UserDto>>builder()
                .data(userService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ResponseData<UserDto> getOne(@PathVariable Long id) {
        return ResponseData
                .<UserDto>builder()
                .data(userService.getById(id))
                .build();
    }
}
