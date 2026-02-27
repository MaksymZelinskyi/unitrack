package com.unitrack.exception;

public class AuthorizationException extends SecurityException {
    public AuthorizationException(String message) {
        super(message);
    }
}
