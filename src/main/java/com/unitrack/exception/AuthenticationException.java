package com.unitrack.exception;

public class AuthenticationException extends SecurityException {

    public AuthenticationException() {}

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
