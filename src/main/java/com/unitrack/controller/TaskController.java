package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.CollaboratorInListDto;
import com.unitrack.dto.request.TaskDto;
import com.unitrack.entity.Project;
import com.unitrack.entity.Task;
import com.unitrack.service.CollaboratorService;
import com.unitrack.service.ProjectService;
import com.unitrack.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class TaskController extends AuthenticatedController {

    private final TaskService taskService;
    private final AuthorizationService authService;
    private final CollaboratorService collaboratorService;
    private final ProjectService projectService;

    @PostMapping("/tasks")
    @PreAuthorize("@authService.canUpdateOrDelete(#principal.getName(), #projectId)")
    public String createTask(TaskDto task, @RequestParam Long projectId,
                           Principal principal) throws IllegalAccessException {
        task.setProjectId(projectId);
        taskService.add(task, projectId);
        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/tasks/new")
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

    @GetMapping("/tasks/{id}")
    public String getTaskById(@PathVariable Long id) {
        return "task";
    }

    @PutMapping("/tasks/{id}")
    @PreAuthorize("@authService.canUpdateOrDelete(#principal.getName(), #projectId)")
    public Task updateTask(@PathVariable Long id, @RequestParam Long projectId,
                           @RequestBody TaskDto task, Principal principal) throws IllegalAccessException {
        return taskService.update(id, task);
    }

    @DeleteMapping("/tasks/{id}")
    @PreAuthorize("@authService.canUpdateOrDelete(#principal.getName(), #projectId)")
    public void deleteTask(@PathVariable Long id, @RequestParam Long projectId, Principal principal) throws IllegalAccessException {
        taskService.deleteById(id);
    }
}
