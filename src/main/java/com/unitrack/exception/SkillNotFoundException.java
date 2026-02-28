package com.unitrack.exception;

public class SkillNotFoundException extends EntityNotFoundException {
    public SkillNotFoundException() {}

    public SkillNotFoundException(String message) {
        super(message);
    }

    public SkillNotFoundException(Throwable cause) {
        super(cause);
    }

    public SkillNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public <T> SkillNotFoundException(String property, T value) {
        this(String.format(TEMPLATE, "Skill", property, value));
    }

}
