package com.unitrack.config;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Participation;
import com.unitrack.entity.Project;
import com.unitrack.entity.Role;
import com.unitrack.exception.AuthenticationException;
import com.unitrack.exception.EntityNotFoundException;
import com.unitrack.exception.ProjectNotFoundException;
import com.unitrack.exception.SecurityException;
import com.unitrack.repository.AssignmentRepository;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("authService")
@RequiredArgsConstructor
public class AuthorizationService {

    private final AssignmentRepository assignmentRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final ProjectRepository projectRepository;

    public boolean canUpdateOrDelete(String email, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("id", projectId));
        Collaborator collaborator = collaboratorRepository.findByEmail(email).orElseThrow(() -> new AuthenticationException("Collaborator with email " + email + " not found."));
        Participation participation = assignmentRepository.findFirstByProjectAndCollaborator(project, collaborator);

        if(participation == null) return isAdmin(email);
        Set<Role> roles = participation.getRoles();
        return isAdmin(email) || roles.contains(Role.PRODUCT_OWNER) || roles.contains(Role.PROJECT_MANAGER);
    }

    public boolean isAdmin(String email) {
        Collaborator collaborator = collaboratorRepository
                .findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Collaborator with email " + email + " not found."));
        return collaborator.isAdmin();
    }

}
