package com.unitrack.service;

import com.unitrack.dto.WorkspaceDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Workspace;
import com.unitrack.exception.CollaboratorNotFoundException;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.WorkspaceRepository;
import com.unitrack.util.mapper.WorkspaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final CollaboratorRepository collaboratorRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMapper workspaceMapper;


    public Workspace getUserWorkspace(String userEmail) {
        Collaborator currentUser = collaboratorRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CollaboratorNotFoundException("email", userEmail));
        return null;
    }

    public Page<WorkspaceDto> searchWorkspaces(String query, Pageable pageable) {
        Page<Workspace> workspaces = workspaceRepository.findByNameContainingOrDescriptionContaining(query, pageable);
        List<WorkspaceDto> dtoList = workspaces.stream().map(workspaceMapper::workspaceToDto).toList();

        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }


}
