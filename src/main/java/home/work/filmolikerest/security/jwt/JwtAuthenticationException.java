package home.work.filmolikerest.security.jwt;

import org.springframework.security.core.AuthenticationException;

/**
 * Authentication exception for application.
 */


public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
