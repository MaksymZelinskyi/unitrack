package com.unitrack.config;

import com.unitrack.entity.*;
import com.unitrack.exception.*;
import com.unitrack.exception.SecurityException;
import com.unitrack.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("authService")
@RequiredArgsConstructor
public class AuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationService.class);
    private final AssignmentRepository assignmentRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    public boolean canUpdateOrDelete(String email, Long projectId) {
        log.debug("Authorizing user {} to update or delete project with id {}", email, projectId);
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("id", projectId));
        Collaborator collaborator = getUser(email);
        Participation participation = assignmentRepository.findFirstByProjectAndCollaborator(project, collaborator);

        if(participation == null) return isAdmin(email);
        Set<Role> roles = participation.getRoles();
        return isAdmin(email) || roles.contains(Role.PRODUCT_OWNER) || roles.contains(Role.PROJECT_MANAGER);
    }

    public boolean canUpdateOrDeleteTask(String email, Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("id", taskId));
        return canUpdateOrDelete(email, task.getProject().getId());
    }

    public boolean canViewTask(String email, Long taskId) {
        return isAdmin(email) || isEngagedInTask(email, taskId);
    }

    public boolean canUpdateComment(String email, Long commentId) {
        Collaborator collaborator = getUser(email);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("id", commentId));
        return comment.getAuthor().equals(collaborator);
    }

    public boolean canDeleteComment(String email, Long commentId) {
        return isAdmin(email) || canUpdateComment(email, commentId);
    }

    public boolean isAdmin(String email) {
        Collaborator collaborator = getUser(email);
        return collaborator.isAdmin();
    }

    public boolean isEngagedInTask(String email, Task task) {
        Collaborator collaborator = getUser(email);
        return task.getAssignees().contains(collaborator);
    }

    public boolean isEngagedInTask(String email, Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("id", taskId));
        return isEngagedInTask(email, task);
    }

    public Collaborator getUser(String email) {
        Collaborator collaborator = collaboratorRepository
                .findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Collaborator with email " + email + " not found."));
        return collaborator;
    }
}