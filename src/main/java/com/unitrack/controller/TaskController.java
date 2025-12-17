package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.CollaboratorInListDto;
import com.unitrack.dto.CommentDto;
import com.unitrack.dto.request.*;
import com.unitrack.entity.*;
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
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController extends AuthenticatedController {

    private final TaskService taskService;
    private final CollaboratorService collaboratorService;
    private final ProjectService projectService;
    private final AuthorizationService authorizationService;

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
                        .sorted(Comparator.comparing(x -> x.getName()))
                        .toList());
        model.addAttribute("taskForm", new TaskDto());
        model.addAttribute("assignees", new ArrayList<>());
        return "new-task";
    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable Long id, Model model, Principal principal) {
        Task task = taskService.getById(id);
        Project project = task.getProject();

        boolean canUpdateDelete = authorizationService.canUpdateOrDeleteTask(principal.getName(), id);
        Set<Participation> assignees = project.getAssignees();
        Map<Long, Role> roles = new HashMap<>();
        assignees.forEach(x -> roles.put(x.getCollaborator().getId(),
                x.getRoles().stream().findFirst().orElse(null))
        );

        model.addAttribute("canUpdate", canUpdateDelete);
        model.addAttribute("canDelete", canUpdateDelete);
        model.addAttribute("task",
                new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getProject().getId(),
                        task.getStatus().name(), task.getDeadline(),
                        task.getAssignees().stream().map(x ->
                                new com.unitrack.dto.AssigneeDto(x.getId(), x.getAvatarUrl(), x.getFullName(), String.valueOf(roles.get(x.getId()))
                                )).toList())
        );
        model.addAttribute("comments", getTaskCommentsDto(task));
        model.addAttribute("commentForm", new com.unitrack.dto.request.CommentDto());
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
                .sorted(Comparator.comparing(x -> x.getName()))
                .toList();

        List<CollaboratorInListDto> assignees = task.getAssignees()
                .stream()
                .map(x -> new CollaboratorInListDto(x.getId(), x.getFullName(), x.getAvatarUrl()))
                .sorted(Comparator.comparing(x -> x.getName()))
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
    @PreAuthorize("@authService.canUpdateOrDeleteTask(#principal.getName(), #id)")
    public String deleteTask(@PathVariable Long id, Principal principal, HttpServletRequest request) {
        Project project = taskService.getById(id).getProject();
        taskService.deleteById(id);
        return "redirect:/projects/" + project.getId();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("@authService.canUpdateOrDelete(#principal.getName(), #id)")
    public String updateTaskStatus(@PathVariable Long id, @RequestBody UpdateTaskStatusDto status, HttpServletRequest request, Principal principal) {
        taskService.setTaskStatus(id, status.getNewStatus());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/complete/{id}")
    @PreAuthorize("@authService.canUpdateOrDeleteTask(#principal.getName(), #id)")
    public String markTaskAsCompleted(@PathVariable Long id, @RequestParam(required = false) boolean completed, HttpServletRequest request, Principal principal) {
        taskService.markTaskCompleted(id, completed);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    private List<CommentDto> getTaskCommentsDto(Task task) {
        List<CommentDto> comments = task.getComments()
                .stream()
                .map(x ->{
                    Comment replyTo = x.getReplyTo();
                    String replyToAuthor = replyTo != null ? replyTo.getAuthor().getFirstName() : null;
                    return new CommentDto(x.getId(), x.getText(), replyTo != null ? replyTo.getId() : null, replyToAuthor,
                            new CollaboratorInListDto(
                                    x.getAuthor().getId(), x.getAuthor().getFullName(), x.getAuthor().getAvatarUrl()
                            ), x.getCreatedAt()
                    );
                }
                ).toList();
        log.debug("Fetched {} comments for task {}", comments.size(), task.getId());
        return comments;
    }
}