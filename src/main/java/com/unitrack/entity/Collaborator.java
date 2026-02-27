package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"projects", "skills", "tasks", "workspace"})
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    @NaturalId
    private String email;
    private String password;
    private String avatarUrl;

    @ManyToMany
    private Set<Skill> skills = new HashSet<>();
    @OneToMany(mappedBy = "collaborator", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Participation> projects = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "assignees")
    private Set<Task> tasks = new HashSet<>();
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Workspace workspace;

    @CreationTimestamp
    private LocalDate joinDate;
    private boolean isAdmin;
  
    @Enumerated(EnumType.STRING)
    private Set<AuthProvider> authProviders = new HashSet<>();

    public Collaborator(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Collaborator(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Collaborator(String firstName, String lastName, String email, String password, Workspace workspace) {
        this(firstName, lastName, email, password);
        this.workspace = workspace;
    }

    public void addProject(Project project, Role role) {
        Participation participation = new Participation(this, project, role);
        addProject(participation);
    }

    public void addProject(Participation participation) {
        projects.add(participation);
    }

    public void addTask(Task task) {tasks.add(task);}

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public void deleteSkill(Skill skill) {
        skills.remove(skill);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstNameAndFirstLetter() {
        if(lastName == null) return firstName;
        return firstName + " " + lastName.substring(0, 1);
    }

    public void addAuthProvider(AuthProvider authProvider) {
        this.authProviders.add(authProvider);
    }
}
