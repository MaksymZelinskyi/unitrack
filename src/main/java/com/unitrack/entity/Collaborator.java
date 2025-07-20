package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Collaborator {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @ManyToMany
    private Set<Skill> skills = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Participation> projects = new HashSet<>();

    private LocalDate joinDate;
    private boolean isAdmin;

    public Collaborator(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public void addProject(Project project, Role role) {
        Participation participation = new Participation(this, project, role);
        projects.add(participation);
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public void deleteSkill(Skill skill) {
        skills.remove(skill);
    }

    enum GlobalRole {
        ADMIN, USER
    }
}
