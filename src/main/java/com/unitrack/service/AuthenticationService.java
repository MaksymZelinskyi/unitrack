package com.unitrack.service;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Workspace;
import com.unitrack.exception.CollaboratorNotFoundException;
import com.unitrack.repository.CollaboratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final CollaboratorRepository collaboratorRepository;
    private final WorkspaceService workspaceService;

    public void defineWorkspace(String email) {
        Collaborator collaborator = collaboratorRepository.findByEmail(email).orElseThrow(() -> new CollaboratorNotFoundException("email", email));
        if (collaborator.getWorkspace() == null) {
            Workspace workspace = new Workspace(collaborator.getFullName() + "'s team");
            collaborator.setWorkspace(workspace);
            collaborator.setAdmin(true);
        }
        collaboratorRepository.save(collaborator);
    }
}
