package com.unitrack.exception;

public class ClientNotFoundException extends EntityNotFoundException {

    public ClientNotFoundException() {}

    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(Throwable cause) {
        super(cause);
    }

    public ClientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public <T> ClientNotFoundException(String property, T value) {
        this(String.format(TEMPLATE, "Client", property, value));
    }

}