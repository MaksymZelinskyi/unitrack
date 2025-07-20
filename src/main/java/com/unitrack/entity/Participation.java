package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Participation {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Collaborator collaborator;
    @ManyToOne
    private Project project;

    @Enumerated
    private Set<Role> roles = new HashSet<>();

    public Participation(Collaborator collaborator, Project project, Role role) {
        this.collaborator = collaborator;
        this.project = project;
        roles.add(role);
    }

    public void addRole(Role role) {
        roles.add(role);
    }

}
