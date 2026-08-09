package com.unitrack.exception;

public class InvitationNotFoundException extends EntityNotFoundException {


    public InvitationNotFoundException() {
    }

    public InvitationNotFoundException(String message) {
        super(message);
    }

    public InvitationNotFoundException(Throwable cause) {
        super(cause);
    }

    public InvitationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public <T> InvitationNotFoundException(String property, T value) {
        super(property, value);
    }
}
