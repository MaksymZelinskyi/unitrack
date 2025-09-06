package com.unitrack.controller;

import com.unitrack.dto.CurrentUser;
import com.unitrack.entity.Collaborator;
import com.unitrack.repository.CollaboratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Authentication Controller is a super-class inherited by all the controllers that provide
 * endpoints for authenticated users.
 * It defines the currentUser attribute that is further used in the header elsewhere within the page rendered
 */
@Controller
@SessionAttributes({"currentUser"})
public abstract class AuthenticatedController {

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @ModelAttribute("currentUser")
    public CurrentUser currentUser() {
        CurrentUser currentUser = new CurrentUser();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Collaborator collaborator = collaboratorRepository.findByEmail(auth.getName()).orElseThrow();
            currentUser.setId(collaborator.getId());
            currentUser.setFirstName(collaborator.getFirstName());
            currentUser.setLastName(collaborator.getLastName());
            currentUser.setEmail(collaborator.getEmail());
            currentUser.setAvatarUrl(collaborator.getAvatarUrl());
            currentUser.setStatus(collaborator.isAdmin() ? "Admin" : "User");
        }
        return currentUser;
    }
}
