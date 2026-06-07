package com.unitrack.repository;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.CollaboratorWorkspace;
import com.unitrack.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollaboratorWorkspaceRepository extends JpaRepository<CollaboratorWorkspace, Long> {

    Optional<CollaboratorWorkspace> findByCollaboratorAndWorkspace(Collaborator collaborator, Workspace workspace);

    Optional<CollaboratorWorkspace> findByCollaboratorIdAndWorkspaceId(Long collaboratorId, Long workspaceId);

    boolean existsByCollaboratorAndWorkspace(Collaborator collaborator, Workspace workspace);

}
