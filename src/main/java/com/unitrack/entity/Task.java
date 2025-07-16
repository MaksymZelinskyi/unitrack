package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Task {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;

    @ManyToMany
    private Set<Collaborator> assignees;

    @ManyToOne
    private Project project;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
