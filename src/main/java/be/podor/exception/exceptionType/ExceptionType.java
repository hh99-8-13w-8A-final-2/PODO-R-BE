package be.podor.exception.exceptionType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {
    // 이미지
    NO_IMAGE("NO_IMAGE_EXCEPTION", "업로드 할 수 없는 파일입니다."),
    // 좋아요
    DOUBLE_HEART("DOUBLE_HEART_EXCEPTION", "같은 리뷰에 대해 좋아요를 두 번 지정할 수 없습니다."),
    NO_HEART("NO_HEART_EXCEPTION", "좋아요를 누르지 않은 리뷰입니다."),
    // 극장
    NO_THEATER("NO_THEATER_EXCEPTION", "존재하지 않는 극장입니다."),
    // 뮤지컬
    NO_MUSICAL("NO_MUSICAL_EXCEPTION", "존재하지 않는 뮤지컬입니다."),
    // 리뷰
    NO_REVIEW("NO_REVIEW_EXCEPTION", "해당하는 리뷰가 존재하지 않습니다."),
    // 멤버
    NO_MEMBER("NO_MEMBER_EXCEPTION", "해당하는 멤버가 존재하지 않습니다.");

    private final String exception;
    private final String message;
}
