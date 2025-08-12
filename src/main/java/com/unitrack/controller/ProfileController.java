package com.unitrack.controller;

import com.unitrack.dto.CurrentUser;
import com.unitrack.dto.request.UpdateProfileDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.service.CollaboratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController extends AuthenticatedController {

    private final CollaboratorService collaboratorService;

    @GetMapping("/update")
    public String updateProfile(Principal principal, Model model) {
        Collaborator collaborator = collaboratorService.getByEmail(principal.getName());
        UpdateProfileDto profile = new UpdateProfileDto(collaborator.getFirstName(), collaborator.getLastName(), collaborator.getAvatarUrl(), collaborator.getEmail(), collaborator.getPassword());
        model.addAttribute("profile", profile);

        return "update-profile";
    }

    @PutMapping
    public String updateProfile(UpdateProfileDto dto, Principal principal,
                                @ModelAttribute("currentUser") CurrentUser currentUser) {
        collaboratorService.update(principal.getName(), dto);
        currentUser.setFirstName(dto.getFirstName());
        currentUser.setLastName(dto.getLastName());
        currentUser.setEmail(dto.getEmail());
        currentUser.setAvatarUrl(dto.getAvatarUrl());
        return "redirect:/profile";
    }
}