package com.unitrack.exception;

public class WorkspaceNotFoundException extends EntityNotFoundException {
    public WorkspaceNotFoundException() {
    }

    public WorkspaceNotFoundException(String message) {
        super(message);
    }

    public WorkspaceNotFoundException(Throwable cause) {
        super(cause);
    }

    public WorkspaceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public <T> WorkspaceNotFoundException(String property, T value) {
        super(property, value);
    }
}
