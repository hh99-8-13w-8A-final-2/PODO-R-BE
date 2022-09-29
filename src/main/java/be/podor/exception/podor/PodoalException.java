package be.podor.exception.podor;

import be.podor.exception.exceptionType.ExceptionType;
import lombok.Getter;

import static be.podor.exception.exceptionType.ExceptionType.*;

@Getter
public class PodoalException extends RuntimeException {
    // 이미지
    public static final PodoalException NO_IMAGE_EXCEPTION = new PodoalException(NO_IMAGE);
    // 좋아요
    public static final PodoalException DOUBLE_HEART_EXCEPTION = new PodoalException(DOUBLE_HEART);
    public static final PodoalException NO_HEART_EXCEPTION = new PodoalException(NO_HEART);
    // 극장
    public static final PodoalException NO_THEATER_EXCEPTION = new PodoalException(NO_THEATER);
    // 뮤지컬
    public static final PodoalException NO_MUSICAL_EXCEPTION = new PodoalException(NO_MUSICAL);
    // 리뷰
    public static final PodoalException NO_REVIEW_EXCEPTION = new PodoalException(NO_REVIEW);

    private final ExceptionType exceptionType;

    public PodoalException(ExceptionType type) {
        super(type.getMessage());

        this.exceptionType = type;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
