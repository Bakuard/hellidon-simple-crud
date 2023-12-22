package com.bakuard.simpleCrud.exception;

public class DuplicateGroupException extends RuntimeException {

    public DuplicateGroupException() {
    }

    public DuplicateGroupException(String message) {
        super(message);
    }

    public DuplicateGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateGroupException(Throwable cause) {
        super(cause);
    }

    public DuplicateGroupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
