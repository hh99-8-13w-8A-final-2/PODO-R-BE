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
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class PodorExceptionHandler {

    private final String MAX_FILESIZE_OVER_MESSAGE = "최대 입력 가능한 파일 사이즈를 초과하였습니다. (단일 파일 10MB)";

    private final String WRONG_INPUT_MESSAGE = "입력값을 다시 확인해주세요.";

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
        return ResponseEntity.badRequest().body(WRONG_INPUT_MESSAGE);
    }

    // 이미지 업로드 예외
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(MAX_FILESIZE_OVER_MESSAGE);
    }

    // Podoal 통합 예외
    @ExceptionHandler(PodoalException.class)
    public ResponseEntity<?> handlePodoalException(PodoalException exception) {
        log.warn(exception.getExceptionType().getException() + ": " + exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
