package be.podor.exception.podor;

import be.podor.exception.exceptionType.ExceptionType;

import static be.podor.exception.exceptionType.ExceptionType.*;

public class PodoalException extends RuntimeException {
    // 이미지
    public static final PodoalException NO_IMAGE_EXCEPTION = new PodoalException(NO_IMAGE_MESSAGE);
    // 좋아요
    public static final PodoalException DOUBLE_HEART_EXCEPTION = new PodoalException(DOUBLE_HEART_MESSAGE);
    public static final PodoalException NO_HEART_EXCEPTION = new PodoalException(NO_HEART_MESSAGE);

    public PodoalException(ExceptionType type) {
        super(type.getMessage());
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
