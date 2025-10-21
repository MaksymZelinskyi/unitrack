package com.unitrack.exception;

public class CollaboratorNotFoundException extends EntityNotFoundException {

    public CollaboratorNotFoundException() {}

    public CollaboratorNotFoundException(String message) {
        super(message);
    }

    public CollaboratorNotFoundException(Throwable cause) {
        super(cause);
    }

    public CollaboratorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public <T> CollaboratorNotFoundException(String property, T value) {
        this(String.format(TEMPLATE, "Collaborator", property, value));
    }

}
