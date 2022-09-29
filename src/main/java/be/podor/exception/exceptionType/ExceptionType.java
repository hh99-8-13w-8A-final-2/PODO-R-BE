package be.podor.exception.exceptionType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {
    NO_IMAGE_MESSAGE("업로드 할 수 없는 파일입니다.");

    private final String message;
}
