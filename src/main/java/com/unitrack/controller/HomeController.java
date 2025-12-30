package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.*;
import com.unitrack.entity.*;
import com.unitrack.exception.AuthenticationException;
import com.unitrack.service.ProjectService;
import com.unitrack.service.CollaboratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController extends AuthenticatedController {

    private final AuthorizationService authorizationService;
    private final CollaboratorService collaboratorService;
    private final ProjectService projectService;

    @GetMapping
    public String home() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String getHome(Principal principal, Model model) {
        if (authorizationService.isAdmin(principal.getName())) {
            return getAdminHome(principal, model);
        } else {
            return getUserHome(principal, model);
        }
    }

    public String getUserHome(Principal principal, Model model) {
        Collaborator collaborator = collaboratorService
                .getByEmail(principal.getName());
        List<ProjectParticipationDto> projects = collaborator.getProjects()
                .stream()
                .sorted(Comparator.comparing(Participation::getProject))
                .map(x -> {
                    Project project = x.getProject();
                    return new ProjectParticipationDto(project.getId(), project.getTitle(), project.getDescription(),
                            x.getRoles()
                                    .stream()
                                    .map(Role::toString)
                                    .findFirst().orElse(""),
                                    project.getStatus().name(),
                            project.getEnd()
                    );
                })
                .collect(Collectors.toList());
        log.debug("{} projects extracted for collaborator {}", projects.size(), collaborator.getFirstName());

        model.addAttribute(
                "projects", projects);

        Set<CollaboratorTaskDto> tasks = collaborator.getTasks()
                .stream()
                .map(x -> new CollaboratorTaskDto(x.getId(), x.getTitle(), x.getDescription(), x.getProject().getTitle(), x.getDeadline()))
                .collect(Collectors.toSet());
        log.debug("{} tasks extracted for collaborator {}", tasks.size(), collaborator.getFirstName());
        model.addAttribute("tasks", tasks);
        return "home";
    }

    public String getAdminHome(Principal principal, Model model) {
        List<Project> projects = projectService.getAllSorted(principal.getName());
        List<Collaborator> collaborators = collaboratorService.getAll(principal.getName());
        List<ProjectDto> dtoList = new ArrayList<>();
        model.addAttribute(
                "projects",
                projects
                        .stream()
                        .map(x -> new ProjectDto(x.getId(), x.getTitle(), x.getDescription(),
                                x.getClient() != null ? new ProjectClientDto(x.getClient().getId(), x.getClient().getName()) : null,
                                x.getStart(), x.getEnd(), x.getStatus().name()))
                        .toList()
        );
        model.addAttribute("collaborators", collaborators
                .stream()
                .map(x ->
                        new CollaboratorDto(
                                x.getId(),
                                x.getFullName(),
                                x.getAvatarUrl(),
                                x.getSkills().stream().map(Skill::getName).toList(),
                                x.getProjects().stream().map(y -> y.getProject().getTitle()).collect(Collectors.toList())
                        )
                )
                .sorted(Comparator.comparing(x -> x.name))
                .toList());
        return "admin-page";
    }
}