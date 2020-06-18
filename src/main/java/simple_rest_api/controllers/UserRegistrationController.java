package simple_rest_api.controllers;


import simple_rest_api.model.dto.CreateUserDto;
import simple_rest_api.model.dto.response.ResponseData;
import simple_rest_api.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/security")
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    /**
     * I would like to return id of added user.
     * @param createUserDto - passing createUserDto (localhost:8096/security)
     * @return id of added user
     */

    @PostMapping
    public ResponseEntity<ResponseData<Long>> register(@RequestBody CreateUserDto createUserDto) {

        System.out.println(createUserDto);
        var responseData = ResponseData
                .<Long>builder()
                .data(userRegistrationService.create(createUserDto))
                .build();

        var headers = new HttpHeaders();
        return new ResponseEntity<>(responseData, headers, HttpStatus.CREATED);

    }
}
