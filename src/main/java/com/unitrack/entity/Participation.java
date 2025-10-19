package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public String toString() {
        return "Participation: {\n" +
                "id: " + id +",\n" +
                "Collaborator: " + collaborator.getFirstNameAndFirstLetter() +",\n" +
                "Project: " + project.getTitle();
    }
}
