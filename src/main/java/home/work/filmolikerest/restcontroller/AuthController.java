package home.work.filmolikerest.restcontroller;

import home.work.filmolikerest.dto.AuthRequestDto;
import home.work.filmolikerest.dto.RegistrationRequestDto;
import home.work.filmolikerest.enums.RegistrationStatus;
import home.work.filmolikerest.model.User;
import home.work.filmolikerest.security.jwt.JwtTokenProvider;
import home.work.filmolikerest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for authentication requests (login, logout, register, etc.)
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthController
{
//    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequestDto requestDto) {
        String username = requestDto.getUsername();
        String email = requestDto.getEmail();

        RegistrationStatus answer = userService.register(requestDto.toUser());

        switch (answer) {
            case SUCCESS: {
                return ResponseEntity.ok("User has been registered successful");
            }
            case USERNAME_AND_EMAIL_EXIST: {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(String.format("User with username= \"%s\" and email= \"%s\" already registered", username, email));
            }
            case USERNAME_EXIST: {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(String.format("User with username= \"%s\" already registered", username));
            }
            case EMAIL_EXIST: {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(String.format("User with email= \"%s\" already registered", email));
            }
            default: {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto request) {
        String username = request.getUsername();

        User user = userService.findByUsernameAndPassword(username, request.getPassword());

        if (user.equals(User.NULL_USER)) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtTokenProvider.createToken(user);

        Map<Object, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
