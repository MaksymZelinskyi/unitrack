package com.unitrack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "join_request")
@Data
@NoArgsConstructor
public class JoinRequest extends Message {

    @ManyToOne
    private Collaborator collaborator;

    @ManyToOne
    private Workspace workspace;

    private LocalDateTime expiresAt;

    public JoinRequest(Collaborator collaborator, Workspace workspace, Collaborator workspaceAdmin) {
        this.collaborator = collaborator;
        this.workspace = workspace;
        this.sender = collaborator;
        this.recipient = workspaceAdmin;
    }
}