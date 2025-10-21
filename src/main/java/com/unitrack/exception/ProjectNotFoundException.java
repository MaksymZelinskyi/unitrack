package com.unitrack.exception;

public class ProjectNotFoundException extends EntityNotFoundException {

    public <T> ProjectNotFoundException(String property, T value) {
        super(String.format(TEMPLATE, "Project", property, value));
    }

}
