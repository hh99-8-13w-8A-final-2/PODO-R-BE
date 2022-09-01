package be.podor.exception;

import be.podor.slack.SlackWebhook;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class PodorExceptionHandler {

    private final SlackWebhook slackWebhook;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException exception) {
        String errorMessage = exception.getMessage();

        // 슬랙 웹훅 전달
        slackWebhook.postSlackMessage("[server error] - " + errorMessage);

        return ResponseEntity.badRequest().body(errorMessage);
    }
}
