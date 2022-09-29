package be.podor.exception.podor;

import be.podor.exception.exceptionType.ExceptionType;

import static be.podor.exception.exceptionType.ExceptionType.NO_IMAGE_MESSAGE;

public class PodoalException extends RuntimeException {
    public static final PodoalException NO_IMAGE_EXCEPTION = new PodoalException(NO_IMAGE_MESSAGE);

    public PodoalException(ExceptionType type) {
        super(type.getMessage());
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
