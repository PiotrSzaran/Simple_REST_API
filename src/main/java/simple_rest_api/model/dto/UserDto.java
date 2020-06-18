package simple_rest_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import simple_rest_api.model.Role;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String email;
    private Role role;
    private Boolean enabled;
    private Set<AdvertisementDto> advertisements;
}