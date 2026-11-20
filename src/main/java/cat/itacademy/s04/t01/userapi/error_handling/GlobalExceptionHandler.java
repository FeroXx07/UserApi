package cat.itacademy.s04.t01.userapi.error_handling;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 500 - Generic exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiError response = new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), "An unexpected error occurred", request.getRequestURI(), null);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiError response = new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), "Validation failed", request.getRequestURI(), errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApiError> handleResourceFoundException(Exception ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiError response = new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), "RESOURCE_NOT_FOUND", request.getRequestURI(), null);

        return ResponseEntity.status(status).body(response);
    }

}
