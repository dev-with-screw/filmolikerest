package home.work.filmolikerest.security;

import home.work.filmolikerest.model.User;
import home.work.filmolikerest.repository.UserRepository;
import home.work.filmolikerest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of {@link UserDetailsService} interface for {@link User}.
 */

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userService.findByUsername(username);

        if (user.equals(User.NULL_USER)) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        log.info("IN loadUserByUsername - user with username: {} successfully loaded", username);

        return user;
    }
}
