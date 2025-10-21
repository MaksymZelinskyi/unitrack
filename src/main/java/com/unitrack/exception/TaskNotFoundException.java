package com.unitrack.exception;

public class TaskNotFoundException extends EntityNotFoundException {

    public <T> TaskNotFoundException(String property, T value) {
        super(String.format(TEMPLATE, "Task", property, value));
    }
}
