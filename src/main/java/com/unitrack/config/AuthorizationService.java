package com.unitrack.config;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Participation;
import com.unitrack.entity.Project;
import com.unitrack.entity.Role;
import com.unitrack.repository.AssignmentRepository;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AssignmentRepository assignmentRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final ProjectRepository projectRepository;

    public boolean hasRole(String email, Long projectId, Role role) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Collaborator collaborator = collaboratorRepository.findByEmail(email).orElseThrow();
        Participation participation = assignmentRepository.findByProjectAndCollaborator(project, collaborator);

        return participation.getRoles().contains(role);
    }

}
