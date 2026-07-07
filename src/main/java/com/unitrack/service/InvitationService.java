package com.unitrack.service;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.CollaboratorWorkspace;
import com.unitrack.entity.Workspace;
import com.unitrack.exception.CollaboratorNotFoundException;
import com.unitrack.exception.WorkspaceNotFoundException;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.CollaboratorWorkspaceRepository;
import com.unitrack.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final CollaboratorRepository collaboratorRepository;
    private final WorkspaceRepository workspaceRepository;
    private final CollaboratorWorkspaceRepository collaboratorWorkspaceRepository;


    public void inviteCollaborator(Long workspaceId, Long collaboratorId, String currentUserEmail) {
        Collaborator collaborator = collaboratorRepository.findById(collaboratorId)
                .orElseThrow(() -> new CollaboratorNotFoundException("id", collaboratorId));
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException("id", workspaceId));

        workspace.addCollaborator(collaborator);
        CollaboratorWorkspace cw = new CollaboratorWorkspace(collaborator, workspace);

        collaboratorWorkspaceRepository.save(cw);
    }
}
