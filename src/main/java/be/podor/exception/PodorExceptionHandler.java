package be.podor.exception;

import be.podor.exception.podor.PodoalException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    // 리프래시 토큰 재발급 예외
    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<?> handleUnsupportedJwtException(UnsupportedJwtException exception) {
        String errorMessage = exception.getMessage();
        log.warn(errorMessage, exception.getCause());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
    }

    // @Valid 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]\n");
        }
        log.warn(builder.toString());
        return ResponseEntity.badRequest().body(builder.toString());
    }

    // Podoal 통합 예외
    @ExceptionHandler(PodoalException.class)
    public ResponseEntity<?> handlePodoalException(PodoalException exception) {
        log.warn(exception.getExceptionType().getException() + ": " + exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
