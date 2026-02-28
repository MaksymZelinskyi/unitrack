package com.unitrack.exception;

public class RegistrationException extends AuthenticationException {


    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistrationException(String message) {
        super(message);
    }

    public RegistrationException() {
    }

    public RegistrationException(Throwable cause) {
        super(cause);
    }
}
