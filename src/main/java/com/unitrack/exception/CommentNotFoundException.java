package com.unitrack.exception;

public class CommentNotFoundException extends EntityNotFoundException {

    public <T> CommentNotFoundException(String property, T value) {
        super(String.format(TEMPLATE, "Comment", property, value));
    }
}
