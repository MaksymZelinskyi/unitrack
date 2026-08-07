package com.unitrack.repository;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.JoinRequest;
import com.unitrack.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    boolean existsByWorkspaceAndCollaborator(Workspace workspace, Collaborator collaborator);
}
