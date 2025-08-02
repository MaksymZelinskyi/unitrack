package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.CollaboratorInListDto;
import com.unitrack.dto.request.TaskDto;
import com.unitrack.entity.Project;
import com.unitrack.entity.Role;
import com.unitrack.entity.Task;
import com.unitrack.service.CollaboratorService;
import com.unitrack.service.ProjectService;
import com.unitrack.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class TaskController extends AuthenticatedController {

    private final TaskService taskService;
    private final AuthorizationService authService;
    private final CollaboratorService collaboratorService;
    private final ProjectService projectService;

    @PostMapping("/tasks")
    public String createTask(TaskDto task, @RequestParam Long projectId,
                           Principal principal) throws IllegalAccessException {
        task.setProjectId(projectId);
        taskService.add(task, projectId);
        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/tasks/new")
    public String createTask(@RequestParam Long projectId, Model model) {
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
    public Task updateTask(@PathVariable Long id,
                           @RequestBody TaskDto task, Principal principal) throws IllegalAccessException {
        if(!authService.hasRole(principal.getName(), task.getProjectId(), Role.PROJECT_MANAGER))
            throw new IllegalAccessException();
        return taskService.update(id, task);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Long id,
                           @RequestBody TaskDto task, Principal principal) throws IllegalAccessException {
        if(!authService.hasRole(principal.getName(), task.getProjectId(), Role.PROJECT_MANAGER))
            throw new IllegalAccessException();
        taskService.deleteById(id);
    }
}
