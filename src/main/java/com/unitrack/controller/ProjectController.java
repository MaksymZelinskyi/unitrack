package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.ProjectClientDto;
import com.unitrack.dto.CollaboratorInListDto;
import com.unitrack.dto.ProjectTaskDto;
import com.unitrack.dto.request.AssigneeDto;
import com.unitrack.dto.request.ProjectDto;
import com.unitrack.dto.request.UpdateProjectDto;
import com.unitrack.entity.Client;
import com.unitrack.entity.Participation;
import com.unitrack.entity.Project;
import com.unitrack.service.ClientService;
import com.unitrack.service.CollaboratorService;
import com.unitrack.service.ProjectService;
import com.unitrack.service.TaskService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
@SessionAttributes({"collaborators", "assignees"})
@Slf4j
public class ProjectController extends AuthenticatedController {

    private final ProjectService projectService;
    private final TaskService taskService;
    private final CollaboratorService collaboratorService;
    private final AuthorizationService authService;
    private final ClientService clientService;

    @GetMapping("/{id}")
    public String getProjectById(@PathVariable Long id, Model model, Principal principal) {
        Project project = projectService.getById(id);
        List<ProjectTaskDto> tasks = taskService.getByProject(project)
                .stream()
                .map(x -> new ProjectTaskDto(x.getId(), x.getTitle(), x.getDescription(),
                        x.getAssignees().stream().map(y -> y.getFirstName() + " " + y.getLastName()).toList(),
                        x.getDeadline(), x.getStatus().name()))
                .toList();
        List<ProjectTaskDto> todo = new ArrayList<>();
        List<ProjectTaskDto> inProgress = new ArrayList<>();
        List<ProjectTaskDto> done = new ArrayList<>();

        for (ProjectTaskDto task : tasks) {
            switch (task.getStatus()) {
                case "TODO":
                    todo.add(task);
                    break;
                case "IN_PROGRESS":
                    inProgress.add(task);
                    break;
                case "DONE":
                    done.add(task);
            }
        }
        Client client = project.getClient();
        ProjectClientDto clientDto = null;
        if (client != null) clientDto = new ProjectClientDto(client.getId(), client.getName());
        model.addAttribute("project",
                new com.unitrack.dto.ProjectDto(project.getId(), project.getTitle(), project.getDescription(), clientDto,
                        project.getStart(), project.getEnd(), project.getStatus().name()
                )
        );
        model.addAttribute("todo", todo);
        model.addAttribute("in_progress", inProgress);
        model.addAttribute("done", done);

        boolean canUpdateDelete = authService.canUpdateOrDelete(principal.getName(), id);
        model.addAttribute("canUpdate", canUpdateDelete);
        model.addAttribute("canDelete", canUpdateDelete);
        return "project";
    }

    @GetMapping("/new")
    public String newProject(Model model) {
        ProjectDto projectForm = new ProjectDto();
        List<CollaboratorInListDto> collaborators = collaboratorService.getAll()
                .stream()
                .map(x -> new CollaboratorInListDto(x.getId(), x.getFirstName() + " " + x.getLastName(), x.getAvatarUrl()))
                .sorted(Comparator.comparing(x -> x.getName()))
                .toList();
        List<ProjectClientDto> clients = clientService.getAll().stream().map(x -> new ProjectClientDto(x.getId(), x.getName())).toList();
        model.addAttribute("collaborators", collaborators);
        model.addAttribute("assignees", new ArrayList<>());
        model.addAttribute("projectForm", projectForm);
        model.addAttribute("clients", clients);
        return "new-project";
    }

    @PostMapping("/new")
    public String newProject(@Validated @ModelAttribute("projectForm") ProjectDto dto, Principal principal) {
        log.debug("The assignees of project being created: {}", dto.getAssignees());
        projectService.add(dto, principal.getName());
        return "redirect:/home";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("@authService.canUpdateOrDelete(#principal.getName(), #id)")
    public String updateProject(@PathVariable Long id, Principal principal, Model model) {
        Project project = projectService.getById(id);

        List<AssigneeDto> collaborators = collaboratorService.getAll()
                .stream()
                .map(c -> {
                    Participation participation = c.getProjects().stream().filter(x -> x.getProject().equals(project)).findFirst().orElse(null);
                    var role = participation != null ? participation.getRoles().stream().findFirst().orElse(null) : null;
                    return new AssigneeDto(
                            c.getId(), role != null ? role.name() : null, c.getFullName()
                    );
                }).toList();
        List<AssigneeDto> assignees = projectService.getProjectAssignees(project)
                .stream()
                .map(x -> new AssigneeDto(
                        x.getCollaborator().getId(), x.getRoles().isEmpty() ? "" : x.getRoles().stream().findFirst().orElseThrow().name(),
                        x.getCollaborator().getFullName())
                ).toList();
        model.addAttribute("project",
                new UpdateProjectDto(project.getId(), project.getTitle(), project.getDescription(), project.getClient().getName(),
                        project.getStart(), project.getEnd(),
                        assignees));
        List<ProjectClientDto> clients = clientService.getAll()
                .stream()
                .map(x -> new ProjectClientDto(x.getId(), x.getName()))
                .toList();

        model.addAttribute("collaborators", collaborators);
        model.addAttribute("assignees", assignees);
        model.addAttribute("clients", clients);
        return "update-project";
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authService.canUpdateOrDelete(#principal.getName(), #id)")
    public String updateProject(@PathVariable Long id, @ModelAttribute("projectForm") @Validated UpdateProjectDto project, Principal principal) {
        projectService.update(id, project);
        return "redirect:" + id;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authService.canUpdateOrDelete(#principal.getName(), #id)")
    public String deleteProject(@PathVariable Long id, Principal principal) {
        projectService.delete(id);
        return "redirect:/home";
    }

    @PostMapping("/complete/{id}")
    @PreAuthorize("@authService.isAdmin(#principal.getName())")
    public String markAsCompleted(@PathVariable Long id, @RequestParam(required = false) boolean completed, Principal principal, HttpServletRequest req) {
        log.debug("mark as completed invoked");
        projectService.markAsCompleted(id, completed);
        return "redirect:" + req.getHeader("Referer");
    }

}