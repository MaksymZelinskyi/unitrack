package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitation")
@Data
public class Invitation extends Message {

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
        this.recipient = collaborator;
        this.sender = invitedBy;
    }
}