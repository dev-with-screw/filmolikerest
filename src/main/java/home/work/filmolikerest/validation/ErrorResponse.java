package home.work.filmolikerest.validation;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
public class ErrorResponse {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ErrorResponse(LocalDateTime timestamp, HttpStatus status, String message, List<String> errors) {
        super();
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ErrorResponse(LocalDateTime timestamp, HttpStatus status, String message, String error) {
        super();
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        errors = Collections.singletonList(error);
    }
}
