package home.work.filmolikerest.validation;

import home.work.filmolikerest.security.jwt.JwtAuthenticationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler
{
    // handling BindException and MethodArgumentNotValidException
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request)
    {

        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (ObjectError error :ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

        return handleExceptionInternal(ex, errorResponse, headers, errorResponse.getStatus(), request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request)
    {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            errors.add(v.getRootBeanClass().getName() + " " +
                    v.getPropertyPath() + ": " + v.getMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    //handling TypeMismatchException and MethodArgumentTypeMismatchException
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            WebRequest request)
    {
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatus status,
            @NotNull WebRequest request)
    {

        StringBuilder builder = new StringBuilder();

        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");

        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(
            BadCredentialsException ex,
            WebRequest request)
    {
        String error = ex.getMessage();

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), error);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<Object> handleJwtAuthentication(
            JwtAuthenticationException ex,
            WebRequest request)
    {
        String error = ex.getMessage();

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_ACCEPTABLE, ex.getLocalizedMessage(), error);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "error occurred");

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }
}
