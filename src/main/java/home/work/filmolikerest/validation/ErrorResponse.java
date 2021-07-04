package home.work.filmolikerest.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse
{
    private final LocalDateTime timestamp;
    private final HttpStatus status;
    private final String message;
    private final List<String> errors;

    public ErrorResponse(LocalDateTime timestamp, HttpStatus status, String message, String error) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        errors = Collections.singletonList(error);
    }
}
