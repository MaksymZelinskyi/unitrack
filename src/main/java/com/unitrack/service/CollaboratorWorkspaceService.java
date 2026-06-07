package com.unitrack.service;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.CollaboratorWorkspace;
import com.unitrack.entity.Workspace;
import com.unitrack.exception.SecurityException;
import com.unitrack.repository.CollaboratorWorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollaboratorWorkspaceService {

    private final CollaboratorWorkspaceRepository collaboratorWorkspaceRepository;

    public CollaboratorWorkspace getCollaboratorWorkspace(Long collabId, Long workspaceId) {
        return collaboratorWorkspaceRepository.findByCollaboratorIdAndWorkspaceId(collabId, workspaceId)
                .orElseThrow(() -> new SecurityException("Collaborator-Workspace relation not found"));
    }

    public boolean relationExists(Collaborator collaborator, Workspace workspace) {
        return collaboratorWorkspaceRepository.existsByCollaboratorAndWorkspace(collaborator, workspace);
    }
}
