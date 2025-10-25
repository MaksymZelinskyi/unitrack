package com.unitrack.controller;

import com.unitrack.dto.CollaboratorInListDto;
import com.unitrack.dto.request.*;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Participation;
import com.unitrack.entity.Project;
import com.unitrack.entity.Task;
import com.unitrack.service.CollaboratorService;
import com.unitrack.service.ProjectService;
import com.unitrack.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController extends AuthenticatedController {

    private final TaskService taskService;
    private final CollaboratorService collaboratorService;
    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("@authService.canUpdateOrDelete(#principal.getName(), #projectId)")
    public String createTask(TaskDto task, @RequestParam Long projectId,
                           Principal principal) {
        task.setProjectId(projectId);
        taskService.add(task, projectId);
        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/new")
    @PreAuthorize("@authService.canUpdateOrDelete(#principal.getName(), #projectId)")
    public String createTask(@RequestParam Long projectId, Model model, Principal principal) {
        model.addAttribute("projectId", projectId);
        Project project = projectService.getById(projectId);
        model.addAttribute("collaborators",
                collaboratorService.getCollaboratorsByProject(project)
                        .stream()
                        .map(x -> new CollaboratorInListDto(x.getId(), x.getFirstName()+" "+x.getLastName(), x.getAvatarUrl()))
                        .toList());
        model.addAttribute("taskForm", new TaskDto());
        model.addAttribute("assignees", new ArrayList<>());
        return "new-task";
    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable Long id, Model model) {
        Task task = taskService.getById(id);

        model.addAttribute("task",
                new TaskDto(task.getTitle(), task.getDescription(), task.getProject().getId(), task.getDeadline(),
                        task.getAssignees().stream().map(x ->
                                new CollaboratorInListDto(x.getId(), x.getFullName(), x.getAvatarUrl())
                        ).toList())
        );
        return "task";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("@authService.canUpdateOrDeleteTask(#principal.getName(), #id)")
    public String updateTask(@PathVariable Long id, Principal principal, Model model) {
        Task task = taskService.getById(id);

        Project project = task.getProject();
        List<CollaboratorInListDto> collaborators = collaboratorService.getCollaboratorsByProject(project)
                .stream()
                .map(x -> new CollaboratorInListDto(x.getId(), x.getFullName(), x.getAvatarUrl()))
                .toList();

        List<CollaboratorInListDto> assignees = task.getAssignees()
                .stream()
                .map(x -> new CollaboratorInListDto(x.getId(), x.getFullName(), x.getAvatarUrl()))
                .toList();
        model.addAttribute("task",
                new UpdateTaskDto(task.getId(), task.getTitle(), task.getDescription(),
                        task.getDeadline(), assignees)
        );
        model.addAttribute("collaborators", collaborators);
        model.addAttribute("assignees", assignees);
        return "update-task";
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authService.canUpdateOrDeleteTask(#principal.getName(), #id)")
    public String updateTask(@PathVariable Long id, UpdateTaskDto task, Principal principal) {
        taskService.update(id, task);
        return "redirect:" + id;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authService.canUpdateOrDelete(#principal.getName(), #projectId)")
    public void deleteTask(@PathVariable Long id, @RequestParam Long projectId, Principal principal) {
        taskService.deleteById(id);
    }

    @PutMapping("/{id}/status")
    public String updateTaskStatus(@PathVariable Long id, @RequestBody UpdateTaskStatusDto status, HttpServletRequest request) {
        taskService.setTaskStatus(id, status.getNewStatus());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
