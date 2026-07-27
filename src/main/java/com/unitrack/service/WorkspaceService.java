package com.unitrack.service;

import com.unitrack.dto.WorkspaceDto;
import com.unitrack.dto.request.CreateWorkspaceDto;
import com.unitrack.dto.request.UpdateWorkspaceDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Workspace;
import com.unitrack.exception.CollaboratorNotFoundException;
import com.unitrack.exception.WorkspaceNotFoundException;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.CollaboratorWorkspaceRepository;
import com.unitrack.repository.WorkspaceRepository;
import com.unitrack.util.mapper.WorkspaceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class WorkspaceService {

    private final CollaboratorRepository collaboratorRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMapper workspaceMapper;
    private final CollaboratorWorkspaceRepository collaboratorWorkspaceRepository;

    @Deprecated(forRemoval = true)
    public Workspace getUserWorkspace(String userEmail) {
        Collaborator currentUser = collaboratorRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CollaboratorNotFoundException("email", userEmail));
        return null;
    }

    public Page<WorkspaceDto> searchWorkspaces(String query, Pageable pageable) {
        Page<Workspace> workspaces = workspaceRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query, pageable);

        List<WorkspaceDto> dtoList = workspaces.stream().map(x -> {
            WorkspaceDto dto = workspaceMapper.workspaceToDto(x);
            dto.setId(x.getId());
            dto.setMemberCount(collaboratorWorkspaceRepository.countByWorkspace(x));
            return dto;
        }).toList();

        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }

    public void newWorkspace(CreateWorkspaceDto dto, String userEmail) {
        Collaborator collaborator = collaboratorRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CollaboratorNotFoundException("email", userEmail));
        Workspace workspace = new Workspace(dto.name(), dto.description(), collaborator);

        workspaceRepository.save(workspace);
    }

    public void deleteWorkspace(Long id) {
        workspaceRepository.deleteById(id);
    }

    public Workspace getWorkspace(Long id) {
        return workspaceRepository.findById(id).orElseThrow(() -> new WorkspaceNotFoundException("id", id));
    }

    public void updateWorkspace(Long id, UpdateWorkspaceDto dto) {
        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() -> new WorkspaceNotFoundException("id", id));
        workspace.setName(dto.getName());
        workspace.setDescription(dto.getDescription());

        workspaceRepository.save(workspace);
    }
}
