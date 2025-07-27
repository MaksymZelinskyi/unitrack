package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.request.TaskDto;
import com.unitrack.entity.Role;
import com.unitrack.entity.Task;
import com.unitrack.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final AuthorizationService authService;

    @PostMapping("/tasks")
    public void createTask(@RequestBody TaskDto task,
                           Principal principal) throws IllegalAccessException {
        if(!authService.hasRole(principal.getName(), task.projectId(), Role.PROJECT_MANAGER))
            throw new IllegalAccessException();
        taskService.add(task);
    }

    @GetMapping("/tasks/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PutMapping("/tasks/{id}")
    public Task updateTask(@PathVariable Long id,
                           @RequestBody TaskDto task, Principal principal) throws IllegalAccessException {
        if(!authService.hasRole(principal.getName(), task.projectId(), Role.PROJECT_MANAGER))
            throw new IllegalAccessException();
        return taskService.update(id, task);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Long id,
                           @RequestBody TaskDto task, Principal principal) throws IllegalAccessException {
        if(!authService.hasRole(principal.getName(), task.projectId(), Role.PROJECT_MANAGER))
            throw new IllegalAccessException();
        taskService.deleteById(id);
    }
}
