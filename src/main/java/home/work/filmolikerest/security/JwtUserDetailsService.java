package home.work.filmolikerest.security;

import home.work.filmolikerest.model.User;
import home.work.filmolikerest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtUserDetailsService implements UserDetailsService
{
    private final UserService userService;

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
