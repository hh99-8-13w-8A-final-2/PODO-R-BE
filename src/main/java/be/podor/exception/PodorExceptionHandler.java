package be.podor.exception;

import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class PodorExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException exception) {
        String errorMessage = exception.getMessage();
        log.warn(errorMessage, exception.getCause());
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<?> handleUnsupportedJwtException(UnsupportedJwtException exception) {
        String errorMessage = exception.getMessage();
        log.warn(errorMessage, exception.getCause());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
    }
}
