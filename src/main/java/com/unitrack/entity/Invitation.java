package com.unitrack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitation")
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
}
