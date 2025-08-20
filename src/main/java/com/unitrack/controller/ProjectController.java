package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.CollaboratorInListDto;
import com.unitrack.dto.ProjectTaskDto;
import com.unitrack.dto.request.ProjectDto;
import com.unitrack.dto.request.UpdateAssigneeDto;
import com.unitrack.dto.request.UpdateProjectDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Participation;
import com.unitrack.entity.Project;
import com.unitrack.service.CollaboratorService;
import com.unitrack.service.ProjectService;
import com.unitrack.service.TaskService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@SessionAttributes({"collaborators", "assignees"})
@Slf4j
public class ProjectController extends AuthenticatedController{

    private final ProjectService projectService;
    private final TaskService taskService;
    private final CollaboratorService collaboratorService;
    private final AuthorizationService authService;

    @GetMapping("/{id}")
    public String getProjectById(@PathVariable Long id, Model model, Principal principal) {
        Project project = projectService.getById(id);
        List<ProjectTaskDto> tasks = taskService.getByProject(project)
                .stream()
                .map(x->new ProjectTaskDto(x.getId(), x.getTitle(), x.getDescription(),
                        x.getAssignees().stream().map(y->y.getFirstName() + " " + y.getLastName()).toList(),
                        x.getDeadline(), x.getStatus().name()))
                .toList();
        List<ProjectTaskDto> todo = new ArrayList<>();
        List<ProjectTaskDto> inProgress = new ArrayList<>();
        List<ProjectTaskDto> done = new ArrayList<>();

        for(ProjectTaskDto task : tasks) {
            switch(task.getStatus()) {
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
        model.addAttribute("project",
                new com.unitrack.dto.ProjectDto(project.getId(), project.getTitle(), project.getDescription(),
                        project.getClient()!=null ? project.getClient().getName() : "None", project.getStart(),
                        project.getEnd()));
        model.addAttribute("todo", todo);
        model.addAttribute("in_progress", inProgress);
        model.addAttribute("done", done);

        model.addAttribute("canUpdate", authService.canUpdateOrDelete(principal.getName(), id));
        model.addAttribute("canDelete", authService.canUpdateOrDelete(principal.getName(), id));
        return "project-page";
    }

    @GetMapping("/new")
    public String newProject(Model model) {
        ProjectDto projectForm = new ProjectDto();
        List<CollaboratorInListDto> collaborators = collaboratorService.getAll()
                .stream()
                .map(x-> new CollaboratorInListDto(x.getId(), x.getFirstName()+" "+x.getLastName(), x.getAvatarUrl())).toList();
        model.addAttribute("collaborators", collaborators);
        model.addAttribute("assignees", new ArrayList<>());
        model.addAttribute("projectForm", projectForm);
        return "new-project";
    }

    @PostMapping("/new")
    public String newProject(@Validated ProjectDto dto) {
        dto.getAssignees().forEach(System.out::println);
        projectService.add(dto);
        return "redirect:/home";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("@authService.canUpdateOrDelete(#principal.getName(), #id)")
    public String updateProject(@PathVariable Long id, Principal principal, Model model) {
        Project project = projectService.getById(id);

        List<UpdateAssigneeDto> assignees = collaboratorService.getAll()
                .stream()
                .map(c-> {
                    Participation participation = c.getProjects().stream().filter(x -> x.getProject().equals(project)).findFirst().orElse(null);
                    var role = participation!=null ? participation.getRoles().stream().findFirst().orElse(null) : null;
                    return new UpdateAssigneeDto(
                            c.getId(), c.getFirstName()+" "+c.getLastName(), role!=null ? role.name() : null
                            );
                }).toList();
        List<Participation> participations = projectService.getProjectAssignees(project);
        model.addAttribute("project",
                new UpdateProjectDto(project.getId(), project.getTitle(), project.getDescription(), project.getClient().getName(),
                        project.getStart(), project.getEnd(),
                        participations.stream().map(x -> {
                            Collaborator collaborator = x.getCollaborator();
                            var role = x.getRoles().stream().findFirst().orElseThrow();
                            return new UpdateAssigneeDto(collaborator.getId(), collaborator.getFirstName()+" "+collaborator.getLastName(), role.name());
                        }).toList()));
        model.addAttribute("assignees", assignees);
        return "update-project";
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authService.canUpdateOrDelete(#principal.getName(), #id)")
    public String updateProject(@PathVariable Long id, @Validated UpdateProjectDto project) {
        projectService.update(id, project);
        return "redirect:" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return "redirect:/home";
    }

}
