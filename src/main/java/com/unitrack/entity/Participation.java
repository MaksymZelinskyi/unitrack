package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.Data;

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
    private Role role;

    public Participation(Collaborator collaborator, Project project, Role role) {
        this.collaborator = collaborator;
        this.project = project;
    }

    public enum Role {
        INTERN,
        DEV,
        LEAD_DEV,
        MANAGER
    }
}
