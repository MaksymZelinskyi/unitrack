package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Table(name = "collaborator_workspace")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CollaboratorWorkspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Collaborator collaborator;
    @ManyToOne
    private Workspace workspace;

    @Column(name = "is_admin")
    private boolean isAdmin;
}
