package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.CollaboratorDto;
import com.unitrack.dto.ProjectDto;
import com.unitrack.dto.ProjectParticipationDto;
import com.unitrack.dto.CollaboratorTaskDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Project;
import com.unitrack.entity.Role;
import com.unitrack.entity.Skill;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;
import com.unitrack.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final AuthorizationService authorizationService;
    private final CollaboratorRepository collaboratorRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @GetMapping("/")
    public String getHome(Principal principal, Model model) {
        if(authorizationService.isAdmin(principal.getName())) {
            return getAdminHome(principal, model);
        } else {
            return getUserHome(principal, model);
        }
    }

    public String getUserHome(Principal principal, Model model) {
        Collaborator collaborator = collaboratorRepository.findByEmail(principal.getName()).orElseThrow();
        model.addAttribute("user-id", collaborator.getId());
        model.addAttribute("username", collaborator.getFirstName() + " " + collaborator.getLastName());
        model.addAttribute("status", "User");
        model.addAttribute(
                "projects",
                collaborator.getProjects()
                        .stream()
                        .map(x -> new ProjectParticipationDto(x.getProject().getId(), x.getProject().getTitle(), x.getProject().getDescription(), x.getRoles()
                                .stream()
                                .map(Role::toString)
                                .collect(Collectors.toSet())))
                        .collect(Collectors.toSet())
        );
        model.addAttribute("tasks", taskRepository.findAllByAssigneesContains(collaborator)
                .stream()
                .map(x -> new CollaboratorTaskDto(x.getId(), x.getTitle(), x.getDescription(), x.getProject().getTitle(), x.getDeadline()))
                .collect(Collectors.toSet()));
        return "home";
    }

    public String getAdminHome(Principal principal, Model model) {
        Collaborator collaborator = collaboratorRepository.findByEmail(principal.getName()).orElseThrow();
        List<Project> projects = projectRepository.findAll();
        List<Collaborator> collaborators = collaboratorRepository.findAll();
        model.addAttribute("username", collaborator.getFirstName() + " " + collaborator.getLastName());
        model.addAttribute("status", "Admin");
        model.addAttribute(
                "projects",
                projects
                        .stream()
                        .map(x -> new ProjectDto(x.getId(), x.getTitle(), x.getDescription(), x.getClient().getName(), x.getStart(), x.getEnd()))
                        .collect(Collectors.toSet())
        );
        model.addAttribute("collaborators", collaborators
                .stream()
                .map(x ->
                        new CollaboratorDto(
                                x.getId(),
                                x.getFirstName() + " " + x.getLastName(),
                                x.getSkills().stream().map(Skill::getName).toList(),
                                x.getProjects().stream().map(y -> y.getProject().getTitle()).collect(Collectors.toList())
                        )
                )
                .collect(Collectors.toSet()));
        return "admin-page";
    }
}
