package com.unitrack.service;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.CollaboratorWorkspace;
import com.unitrack.entity.JoinRequest;
import com.unitrack.entity.Workspace;
import com.unitrack.exception.CollaboratorNotFoundException;
import com.unitrack.exception.WorkspaceNotFoundException;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.CollaboratorWorkspaceRepository;
import com.unitrack.repository.JoinRequestRepository;
import com.unitrack.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
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

    private Collaborator findWorkspaceAdmin(Workspace workspace) {
        for (CollaboratorWorkspace cw : workspace.getCollaborators()) {
            if (cw.isAdmin()) return cw.getCollaborator();
        }
        return null;
    }
}
