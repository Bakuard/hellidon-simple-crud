package com.bakuard.simpleCrud.exception;

public class UnknownStudentException extends UnknownEntityException {

    public UnknownStudentException() {
    }

    public UnknownStudentException(String message) {
        super(message);
    }

    public UnknownStudentException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownStudentException(Throwable cause) {
        super(cause);
    }

    public UnknownStudentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
