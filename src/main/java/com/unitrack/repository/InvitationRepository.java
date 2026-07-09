package com.unitrack.repository;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Invitation;
import com.unitrack.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    boolean existsByWorkspaceAndCollaborator(Workspace workspace, Collaborator collaborator);

    void deleteByWorkspaceAndCollaborator(Workspace workspace, Collaborator collaborator);
}
