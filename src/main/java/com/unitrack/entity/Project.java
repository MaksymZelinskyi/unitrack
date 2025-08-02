package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = "assignees")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Participation> assignees = new HashSet<>();

    private LocalDate start;
    private LocalDate end;
    private Status status;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Client client;

    public Project(String title, String description, LocalDate start, LocalDate end) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
    }

    public void addAssignees(Set<Participation> assignees) {
        this.assignees.addAll(assignees);
    }

    public enum Status {
        PLANNED, ACTIVE, DONE
    }
}
