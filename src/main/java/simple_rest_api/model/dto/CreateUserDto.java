package simple_rest_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import simple_rest_api.model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserDto {
    private String name;
    private String surname;
    private String username;
    private String password;
    private String passwordConfirmation;
    private String email;
    private Role role;
    private Boolean enabled;
}