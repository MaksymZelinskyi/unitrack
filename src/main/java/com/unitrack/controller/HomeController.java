package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.ProjectParticipationDto;
import com.unitrack.dto.TaskDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Role;
import com.unitrack.repository.TaskRepository;
import com.unitrack.service.CollaboratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final AuthorizationService authorizationService;
    private final CollaboratorService collaboratorService;
    private final TaskRepository taskRepository;

    @GetMapping("/")
    public String getHome(Principal principal, Model model) {
        if(authorizationService.isAdmin(principal.getName())) {
            return getAdminHome(principal, model);
        } else {
            return getUserHome(principal, model);
        }
    }

    public String getUserHome(Principal principal, Model model) {
        Collaborator collaborator = collaboratorService.getByEmail(principal.getName());
        model.addAttribute("username", collaborator.getFirstName() + " " + collaborator.getLastName());
        model.addAttribute("status", "User");
        model.addAttribute(
                "projects",
                collaborator.getProjects()
                        .stream()
                        .map(x -> new ProjectParticipationDto(x.getProject().getTitle(), x.getProject().getDescription(), x.getRoles()
                                .stream()
                                .map(Role::toString)
                                .collect(Collectors.toSet())))
                        .collect(Collectors.toSet())
        );
        model.addAttribute("tasks", taskRepository.findAllByAssigneesContains(collaborator)
                .stream()
                .map(x -> new TaskDto(x.getTitle(), x.getDescription(), x.getProject().getTitle(), x.getDeadline()))
                .collect(Collectors.toSet()));
        return "home.html";
    }

    public String getAdminHome(Principal principal, Model model) {
        model.addAttribute("status", "Admin");
        return "admin-page.html";
    }
}
