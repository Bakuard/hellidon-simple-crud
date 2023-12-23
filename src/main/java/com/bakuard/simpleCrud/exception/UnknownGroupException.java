package com.bakuard.simpleCrud.exception;

public class UnknownGroupException extends UnknownEntityException {

    public UnknownGroupException() {
    }

    public UnknownGroupException(String message) {
        super(message);
    }

    public UnknownGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownGroupException(Throwable cause) {
        super(cause);
    }

    public UnknownGroupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
