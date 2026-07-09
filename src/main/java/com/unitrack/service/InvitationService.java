package com.unitrack.service;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Invitation;
import com.unitrack.entity.Workspace;
import com.unitrack.exception.CollaboratorNotFoundException;
import com.unitrack.exception.EntityAlreadyExistsException;
import com.unitrack.exception.WorkspaceNotFoundException;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.CollaboratorWorkspaceRepository;
import com.unitrack.repository.InvitationRepository;
import com.unitrack.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final CollaboratorRepository collaboratorRepository;
    private final WorkspaceRepository workspaceRepository;
    private final InvitationRepository invitationRepository;
    private final CollaboratorWorkspaceRepository collaboratorWorkspaceRepository;

    public void inviteCollaborator(Long workspaceId, Long collaboratorId, String currentUserEmail) {
        Collaborator collaborator = collaboratorRepository.findById(collaboratorId)
                .orElseThrow(() -> new CollaboratorNotFoundException("id", collaboratorId));
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException("id", workspaceId));
        Collaborator invitedBy = collaboratorRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new CollaboratorNotFoundException("id", collaboratorId));

        if (collaboratorWorkspaceRepository.existsByCollaboratorAndWorkspace(collaborator, workspace)) {
            throw new EntityAlreadyExistsException("Collaborator-Workspace relation already exists!");
        }

        if (invitationRepository.existsByWorkspaceAndCollaborator(workspace, collaborator)) {
            invitationRepository.deleteByWorkspaceAndCollaborator(workspace, collaborator);
        }
        Invitation invitation = new Invitation(collaborator, workspace, invitedBy);
        invitation.setExpiresAt(LocalDateTime.now().plusMonths(1));

        invitationRepository.save(invitation);
    }
}
