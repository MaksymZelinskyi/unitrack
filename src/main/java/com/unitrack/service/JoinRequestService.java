package com.unitrack.service;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.CollaboratorWorkspace;
import com.unitrack.entity.JoinRequest;
import com.unitrack.entity.Workspace;
import com.unitrack.exception.CollaboratorNotFoundException;
import com.unitrack.exception.ExpirationException;
import com.unitrack.exception.JoinRequestNotFoundException;
import com.unitrack.exception.WorkspaceNotFoundException;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.CollaboratorWorkspaceRepository;
import com.unitrack.repository.JoinRequestRepository;
import com.unitrack.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Join;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class JoinRequestService {

    private final WorkspaceRepository workspaceRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final CollaboratorWorkspaceRepository collaboratorWorkspaceRepository;
    private final JoinRequestRepository joinRequestRepository;

    public void sendJoinRequest(Long workspaceId, String collaboratorEmail) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException("id", workspaceId));
        Collaborator collaborator = collaboratorRepository.findByEmail(collaboratorEmail)
                .orElseThrow(() -> new CollaboratorNotFoundException("email", collaboratorEmail));

        if (collaboratorWorkspaceRepository.existsByCollaboratorAndWorkspace(collaborator, workspace)) {
            log.warn("Collaborator {} is already member of workspace {}", collaboratorEmail, workspaceId);
        } else {
            JoinRequest joinRequest = new JoinRequest(collaborator, workspace, findWorkspaceAdmin(workspace));
            joinRequest.setText(String.format("%s requested to join %s", collaborator.getFullName(), workspace.getName()));
            joinRequest.setExpiresAt(LocalDateTime.now().plusWeeks(1));
            joinRequestRepository.save(joinRequest);
        }
    }

    public void acceptRequest(Long requestId) {
        JoinRequest joinRequest = joinRequestRepository.findById(requestId).orElseThrow(() -> new JoinRequestNotFoundException("id", requestId));
        Workspace workspace = joinRequest.getWorkspace();
        Collaborator collaborator = joinRequest.getCollaborator();
        joinRequestRepository.deleteAllByWorkspaceAndCollaborator(workspace, collaborator);
        if (joinRequest.getExpiresAt().isAfter(LocalDateTime.now())) {
            workspace.addCollaborator(collaborator);
            workspaceRepository.save(workspace);
        } else {
            throw new ExpirationException("Request expired!");
        }
    }

    private Collaborator findWorkspaceAdmin(Workspace workspace) {
        for (CollaboratorWorkspace cw : workspace.getCollaborators()) {
            if (cw.isAdmin()) return cw.getCollaborator();
        }
        return null;
    }

    public boolean requestExists(Workspace workspace, Collaborator collaborator) {
        return joinRequestRepository.existsByWorkspaceAndCollaborator(workspace, collaborator);
    }
}
