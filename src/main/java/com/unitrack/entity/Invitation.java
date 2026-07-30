package com.unitrack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitation")
@Data
public class Invitation {

    @Id
    private Long id;

    @ManyToOne
    private Collaborator collaborator;

    @ManyToOne
    private Workspace workspace;

    @ManyToOne
    private Collaborator invitedBy;

    private LocalDateTime expiresAt;

    public Invitation(Collaborator collaborator, Workspace workspace, Collaborator invitedBy) {
        this.collaborator = collaborator;
        this.workspace = workspace;
        this.invitedBy = invitedBy;
    }
}
