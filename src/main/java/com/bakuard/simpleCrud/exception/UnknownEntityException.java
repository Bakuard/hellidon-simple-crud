package com.bakuard.simpleCrud.exception;

public class UnknownEntityException extends DomainException {

    public UnknownEntityException() {
    }

    public UnknownEntityException(String message) {
        super(message);
    }

    public UnknownEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownEntityException(Throwable cause) {
        super(cause);
    }

    public UnknownEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
