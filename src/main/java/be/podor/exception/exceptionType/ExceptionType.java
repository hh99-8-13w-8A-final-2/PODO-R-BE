package be.podor.exception.exceptionType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {
    NO_IMAGE_MESSAGE("업로드 할 수 없는 파일입니다."),
    DOUBLE_HEART_MESSAGE("같은 리뷰에 대해 좋아요를 두 번 지정할 수 없습니다."),
    NO_HEART_MESSAGE("좋아요를 누르지 않은 리뷰입니다.");

    private final String message;
}
