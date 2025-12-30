package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(mappedBy = "workspace", cascade = CascadeType.REMOVE)
    private Collaborator admin;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Collaborator> collaborators = new HashSet<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Project> projects = new HashSet<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();
}
