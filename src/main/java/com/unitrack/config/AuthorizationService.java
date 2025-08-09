package com.unitrack.config;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Participation;
import com.unitrack.entity.Project;
import com.unitrack.entity.Role;
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
        Project project = projectRepository.findById(projectId).orElseThrow();
        Collaborator collaborator = collaboratorRepository.findByEmail(email).orElseThrow();
        Participation participation = assignmentRepository.findByProjectAndCollaborator(project, collaborator);
        Set<Role> roles = participation.getRoles();
        return roles.contains(Role.PRODUCT_OWNER) || roles.contains(Role.PROJECT_MANAGER);
    }

    public boolean isAdmin(String email) {
        Collaborator collaborator = collaboratorRepository.findByEmail(email).orElseThrow();
        return collaborator.isAdmin();
    }

}
