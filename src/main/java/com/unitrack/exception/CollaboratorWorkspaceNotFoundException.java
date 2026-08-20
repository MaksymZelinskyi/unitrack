package com.unitrack.exception;


public class CollaboratorWorkspaceNotFoundException extends EntityNotFoundException {
  
    public CollaboratorWorkspaceNotFoundException() {}

    public CollaboratorWorkspaceNotFoundException(String message) {
        super(message);
    }

    public CollaboratorWorkspaceNotFoundException(Throwable cause) {
        super(cause);
    }

    public CollaboratorWorkspaceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public <T1, T2> CollaboratorWorkspaceNotFoundException(String p1, T1 v1, String p2, T2 v2) {
        this(String.format("%s with %s %s and %s %s not found.", "CollaboratorWorkspace", p1, v1, p2, v2));
    }
}
