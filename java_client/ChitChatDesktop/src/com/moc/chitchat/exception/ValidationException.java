package com.moc.chitchat.exception;


import org.springframework.validation.Errors;

public class ValidationException extends Exception {

    private Errors errors;

    public ValidationException(Errors errors) {
        super("Validation Exception");

        this.errors = errors;
    }

    public Errors getErrors() {
        return this.errors;
    }
}
