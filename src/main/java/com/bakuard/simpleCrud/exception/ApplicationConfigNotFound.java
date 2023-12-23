package com.bakuard.simpleCrud.exception;

public class ApplicationConfigNotFound extends RuntimeException {

    public ApplicationConfigNotFound() {
    }

    public ApplicationConfigNotFound(String message) {
        super(message);
    }

    public ApplicationConfigNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationConfigNotFound(Throwable cause) {
        super(cause);
    }

    public ApplicationConfigNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
