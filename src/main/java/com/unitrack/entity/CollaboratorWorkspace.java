package com.unitrack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Table(name = "collaborator_workspace")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CollaboratorWorkspace {

    private Long id;
    @ManyToOne
    private Collaborator collaborator;
    @ManyToOne
    private Workspace workspace;

    private boolean isAdmin;
}
