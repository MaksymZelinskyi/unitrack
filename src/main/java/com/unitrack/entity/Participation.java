package com.unitrack.entity;

import jakarta.persistence.*;

@Entity
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

    enum Role {
        INTERN,
        DEV,
        LEAD_DEV,
        MANAGER
    }
}
