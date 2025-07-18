package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Project {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Participation> assignees = new HashSet<>();

    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private Client client;

    public Project(String title, String description, LocalDateTime start, LocalDateTime end, Status status) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.status = status;
    }

    public enum Status {
        PLANNED, ACTIVE, DONE
    }
}
