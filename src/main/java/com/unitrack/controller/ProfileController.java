package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.CurrentUserDto;
import com.unitrack.dto.request.UpdateProfileDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Skill;
import com.unitrack.service.CollaboratorService;
import com.unitrack.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController extends AuthenticatedController {

    private final CollaboratorService collaboratorService;
    private final AuthorizationService authorizationService;
    private final SkillService skillService;

    @GetMapping
    public String getProfile(Principal principal, Model model) {
        Collaborator collaborator = collaboratorService.getByEmail(principal.getName());
        UpdateProfileDto profile = new UpdateProfileDto(collaborator.getFirstName(), collaborator.getLastName(), collaborator.getAvatarUrl(), collaborator.getEmail(), "");
        model.addAttribute("profile", profile);
        return "profile";
    }

    @PutMapping
    public String updateProfile(@Validated UpdateProfileDto dto, Principal principal,
                                @ModelAttribute("currentUser") CurrentUserDto currentUser) {
        Collaborator collaborator = collaboratorService.update(principal.getName(), dto);
        currentUser.setFirstName(collaborator.getFirstName());
        currentUser.setLastName(collaborator.getLastName());
        currentUser.setAvatarUrl(collaborator.getAvatarUrl());
        return "redirect:/profile";
    }

    @PutMapping("/skills")
    public String updateSkillSet(List<Skill> skills, Principal principal) {
        skillService.updateCollaboratorSkills(principal.getName(), skills);
        return "redirect:/profile";
    }
}