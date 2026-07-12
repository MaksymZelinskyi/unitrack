package com.unitrack.service;

import com.unitrack.dto.WorkspaceDto;
import com.unitrack.entity.*;
import com.unitrack.exception.CollaboratorNotFoundException;
import com.unitrack.exception.SecurityException;
import com.unitrack.exception.WorkspaceNotFoundException;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.CollaboratorWorkspaceRepository;
import com.unitrack.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollaboratorWorkspaceService {

    private final CollaboratorWorkspaceRepository collaboratorWorkspaceRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final WorkspaceRepository workspaceRepository;

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
                .map(x -> new WorkspaceDto(x.getId(), x.getName(), x.getDescription(), collaboratorWorkspaceRepository.countByWorkspace(x)))
                .toList();
    }

    public void addCollaboratorWorkspace(String email, Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException("id", workspaceId));
        Collaborator collaborator = collaboratorRepository.findByEmail(email)
                .orElseThrow(() -> new CollaboratorNotFoundException("email", email));

        if (!relationExists(collaborator, workspace)) {
            workspace.addCollaborator(collaborator);
            workspaceRepository.save(workspace);
        } else {
            log.warn("Collaborator {} is already member of workspace {}", email, workspaceId);
        }
    }

    @Transactional
    public void deleteCollaboratorWorkspace(String email, Long workspaceId) {
        collaboratorWorkspaceRepository.deleteByCollaboratorEmailAndWorkspaceId(email, workspaceId);
        log.debug("Deleted collaborator-workspace relation. Workspace {}; Collaborator {}", workspaceId, email);
    }
}
