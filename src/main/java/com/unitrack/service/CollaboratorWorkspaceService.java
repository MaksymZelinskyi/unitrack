package com.unitrack.service;

import com.unitrack.dto.WorkspaceDto;
import com.unitrack.entity.*;
import com.unitrack.exception.CollaboratorNotFoundException;
import com.unitrack.exception.SecurityException;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.CollaboratorWorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CollaboratorWorkspaceService {

    private final CollaboratorWorkspaceRepository collaboratorWorkspaceRepository;
    private final CollaboratorRepository collaboratorRepository;

    public CollaboratorWorkspace getCollaboratorWorkspace(Long collabId, Long workspaceId) {
        return collaboratorWorkspaceRepository.findByCollaboratorIdAndWorkspaceId(collabId, workspaceId)
                .orElseThrow(() -> new SecurityException("Collaborator-Workspace relation not found"));
    }

    public boolean relationExists(Collaborator collaborator, Workspace workspace) {
        return collaboratorWorkspaceRepository.existsByCollaboratorAndWorkspace(collaborator, workspace);
    }

    public Map<Project, Set<Role>> getProjectsWithRoles(Collaborator collaborator, Workspace workspace) {
        Map<Project, Set<Role>> projectRoles = new HashMap<>();

        for (Participation participation : collaborator.getProjects()) {
            if (participation.getProject().getWorkspace().equals(workspace)) {
                projectRoles.put(participation.getProject(), participation.getRoles());
            }
        }
        return projectRoles;
    }

    public List<WorkspaceDto> getWorkspaces(String email) {

        return collaboratorWorkspaceRepository.findAllByCollaboratorEmail(email)
                .stream().map(CollaboratorWorkspace::getWorkspace)
                .map(x -> new WorkspaceDto(x.getId(), x.getName(), x.getDescription()))
                .toList();
    }
}
