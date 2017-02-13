package com.moc.chitchat.exception;

import org.springframework.validation.Errors;

public class ValidationException extends Exception {

    private Errors errors;

    /**
     * Validation Exception is thrown when an incorrect validation has occurred e.g. Duplicate username
     */
    public ValidationException(Errors errors) {
        super("Validation Exception");

        this.errors = errors;
    }

    public Errors getErrors() {
        return this.errors;
    }
}
