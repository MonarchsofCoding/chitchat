package com.moc.chitchat.exception;

import java.util.List;
import java.util.Map;

/**
 * ValidationException provides an Exception containing the Validation errors that occurred.
 */
public class ValidationException extends Exception {

    /*  errors
     */
    private Map<String, List<String>> errors;

    /**
     * ValidationException constructor.
     *
     * @param errors the errors that were generated during validation.
     */
    public ValidationException(Map<String, List<String>> errors) {
        super("Validation Exception!");

        this.errors = errors;
    }

    /**
     * getErrors returns the map of errors associated with the failed validation.
     *
     * @return the errors from the validation.
     */
    public Map<String, List<String>> getErrors() {
        return this.errors;
    }
}
