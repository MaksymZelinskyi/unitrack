package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.*;
import com.unitrack.dto.request.CreateWorkspaceDto;
import com.unitrack.entity.*;
import com.unitrack.service.CollaboratorService;
import com.unitrack.service.CollaboratorWorkspaceService;
import com.unitrack.service.ProjectService;
import com.unitrack.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController extends AuthenticatedController {

    private final AuthorizationService authorizationService;
    private final WorkspaceService workspaceService;
    private final CollaboratorService collaboratorService;
    private final ProjectService projectService;
    private final CollaboratorWorkspaceService collaboratorWorkspaceService;

    @PostMapping("/new")
    public String newWorkspace(CreateWorkspaceDto workspaceDto, Principal principal) {
        workspaceService.newWorkspace(workspaceDto, principal.getName());
        return "redirect:/home";
    }

    @GetMapping("/new")
    public String newWorkspace(Model model) {
        model.addAttribute("workspaceForm", new CreateWorkspaceDto("", ""));
        return "new-workspace";
    }

    @GetMapping("/{id}")
    public String getWorkspace(Principal principal, Model model, @PathVariable("id") Long workspaceId) {
        Workspace workspace = workspaceService.getWorkspace(workspaceId);
        model.addAttribute("workspace", new WorkspaceDto(workspaceId, workspace.getName(), workspace.getDescription()));

        if (authorizationService.isAdmin(principal.getName(), workspaceId)) {
            return getAdminWorkspace(principal, model, workspace);
        } else {
            return getUserWorkspace(principal, model, workspace);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authService.canDeleteWorkspace(#principal.getName(), #id)")
    public String deleteWorkspace(@PathVariable Long id, Principal principal)  {
        workspaceService.deleteWorkspace(id);
        return "redirect:/";
    }

    @GetMapping("/search")
    public Page<WorkspaceDto> searchWorkspace(@RequestParam String query,
                                              @RequestParam(name = "pagenumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(name = "pagesize", defaultValue = "1") int pageSize)  {
        return workspaceService.searchWorkspaces(query, Pageable.ofSize(pageSize).withPage(pageNumber));
    }

    public String getUserWorkspace(Principal principal, Model model, Workspace workspace) {
        Collaborator collaborator = collaboratorService
                .getByEmail(principal.getName());
        Map<Project, Set<Role>> projectRoleMap = collaboratorWorkspaceService.getProjectsWithRoles(collaborator, workspace);

        List<ProjectParticipationDto> projects = workspace.getProjects()
                .stream()
                .sorted()
                .map(x -> new ProjectParticipationDto(x.getId(), x.getTitle(), x.getDescription(),
                            projectRoleMap.containsKey(x) ? projectRoleMap.get(x)
                                    .stream()
                                    .map(Role::toString)
                                    .findFirst().orElse("") : "",
                            x.getStatus().name(),
                            x.getEnd()
                ))
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

    public String getAdminWorkspace(Principal principal, Model model, Workspace workspace) {
        Set<Project> projects = workspace.getProjects();
        List<Collaborator> collaborators = collaboratorService.getAll(principal.getName());
        model.addAttribute(
                "projects",
                projects
                        .stream()
                        .sorted()
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
                .sorted(Comparator.comparing(CollaboratorDto::name))
                .toList());
        return "admin-page";
    }
}
