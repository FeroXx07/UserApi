package cat.itacademy.s04.t01.userapi.error_handling;

import cat.itacademy.s04.t01.userapi.error_handling.exceptions.ResourceAlreadyExists;
import cat.itacademy.s04.t01.userapi.error_handling.exceptions.ResourceNotFound;
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

        ApiError response = new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), "AN_UNEXPECTED_ERROR_OCURRED", request.getRequestURI(), null);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiError response = new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), "VALIDATION_FAILED", request.getRequestURI(), errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApiError> handleResourceFoundException(Exception ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiError response = new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), "RESOURCE_NOT_FOUND", request.getRequestURI(), null);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(Exception ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiError response = new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), "ILLEGAL_ARGUMENT: " + ex.getMessage(), request.getRequestURI(), null);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<ApiError> handleResourceAlreadyExists(Exception ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;

        ApiError response = new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), "RESOURCE_ALREADY_EXISTS: " + ex.getMessage(), request.getRequestURI(), null);

        return ResponseEntity.status(status).body(response);
    }
}
