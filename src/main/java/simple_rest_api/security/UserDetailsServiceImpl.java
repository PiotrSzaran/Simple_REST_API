package simple_rest_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import simple_rest_api.exceptions.AppException;
import simple_rest_api.repository.UserRepository;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Qualifier("customUserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userFromDb = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException("username " + username + " doesn't exist"));

        return new User(
                userFromDb.getUsername(),
                userFromDb.getPassword(),
                userFromDb.getEnabled(), true, true, true,
                Stream.of(userFromDb.getRole().getFullName()).map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }
}
