package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * This entity represents a team with one admin and many collaborators, projects,
 * tasks and clients.
 * All the entities referenced are only related to one workspace;
 * If the workspace is removed, the entities also get removed
 * @see Collaborator
 * @see Project
 * @see Task
 * @see Client
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CollaboratorWorkspace> collaborators = new HashSet<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Project> projects = new HashSet<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();

    public Workspace(String name) {
        this.name = name;
    }

    public Workspace(String name, String description, Collaborator admin) {
        this.name = name;
        this.description = description;
        CollaboratorWorkspace cw = new CollaboratorWorkspace(admin, this);
        cw.setAdmin(true);
        this.collaborators.add(cw);
    }

    public void addCollaborator(Collaborator collaborator) {
        CollaboratorWorkspace cw = new CollaboratorWorkspace(collaborator, this);
        this.collaborators.add(cw);
    }
}