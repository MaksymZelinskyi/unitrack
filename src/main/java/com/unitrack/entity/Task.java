package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    @Enumerated
    private Status status;

    @ManyToMany
    private Set<Collaborator> assignees;

    @ManyToOne
    private Project project;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public enum Status {
        TODO, IN_PROGRESS, DONE
    }
}
