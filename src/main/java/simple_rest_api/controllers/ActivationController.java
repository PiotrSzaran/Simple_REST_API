package simple_rest_api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import simple_rest_api.service.AccountActivationTokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security")
public class ActivationController {

    private final AccountActivationTokenService accountActivationTokenService;

    @GetMapping("/account-activation")
    public Long activateUser(@RequestParam String token) {
        return accountActivationTokenService.activateUser(token);
    }
}
