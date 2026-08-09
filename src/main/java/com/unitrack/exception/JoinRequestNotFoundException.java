package com.unitrack.exception;

public class JoinRequestNotFoundException extends EntityNotFoundException {

    public JoinRequestNotFoundException() {
    }

    public JoinRequestNotFoundException(String message) {
        super(message);
    }

    public JoinRequestNotFoundException(Throwable cause) {
        super(cause);
    }

    public JoinRequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public <T> JoinRequestNotFoundException(String property, T value) {
        super(property, value);
    }
}
