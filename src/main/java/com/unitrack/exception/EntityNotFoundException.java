package com.unitrack.exception;

public class EntityNotFoundException extends RuntimeException{

    public static final String TEMPLATE = "%s with %s %s not found.";

    public EntityNotFoundException() {}

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public <T> EntityNotFoundException(String property, T value) {
        super(String.format(TEMPLATE, "Entity", property, value));
    }
}
