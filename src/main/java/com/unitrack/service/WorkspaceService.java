package com.unitrack.service;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Workspace;
import com.unitrack.exception.CollaboratorNotFoundException;
import com.unitrack.repository.CollaboratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final CollaboratorRepository collaboratorRepository;

    public Workspace getUserWorkspace(String userEmail) {
        Collaborator currentUser = collaboratorRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CollaboratorNotFoundException("email", userEmail));
        return currentUser.getWorkspace();
    }
}
