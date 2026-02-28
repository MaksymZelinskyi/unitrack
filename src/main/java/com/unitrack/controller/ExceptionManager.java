package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.exception.AuthenticationException;
import com.unitrack.exception.EntityNotFoundException;
import com.unitrack.exception.RegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionManager {

    private final AuthorizationService authorizationService;

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(EntityNotFoundException e, Model model, Principal principal) {
        String message = "Something went wrong";

        if(authorizationService.isAdmin(principal.getName()))
            message = e.getMessage();

        model.addAttribute("message", message);
        model.addAttribute("errorCode", 404);
        return "error";
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public String handleAuthException(AuthenticationException e, Model model) {
        model.addAttribute("message", "An authentication error occurred");
        model.addAttribute("errorCode", 403);

        return "error";
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public String handleSecurityException(SecurityException e, Model model) {
        model.addAttribute("message", "A security error occurred");
        model.addAttribute("errorCode", 403);

        return "error";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(RuntimeException e, Model model, Principal principal) {
        String message = "Something went wrong";

        if(principal != null && authorizationService.isAdmin(principal.getName()))
            message = e.getMessage();

        model.addAttribute("message", message);
        model.addAttribute("errorCode", 500);
        return "error";
    }

    @ExceptionHandler(RegistrationException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public String handleRegistrationException(RegistrationException e, Model model, Principal principal) {
        String message = e.getMessage() != null ? e.getMessage() : "An error occurred";

        model.addAttribute("message", message);
        model.addAttribute("errorCode", 401);
        return "error";
    }
}
