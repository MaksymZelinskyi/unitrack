package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "collaborator_workspace")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class CollaboratorWorkspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Collaborator collaborator;
    @ManyToOne(fetch = FetchType.LAZY)
    private Workspace workspace;

    @Column(name = "is_admin")
    private boolean isAdmin;

    public CollaboratorWorkspace(Collaborator collaborator, Workspace workspace) {
        this.collaborator = collaborator;
        this.workspace = workspace;
    }
}
