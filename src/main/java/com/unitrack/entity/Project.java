package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"assignees", "tasks"})
@ToString(exclude = {"assignees", "tasks"})
public class Project implements Comparable<Project> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Participation> assignees = new HashSet<>();
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks = new HashSet<>();

    private LocalDate start;
    private LocalDate end;

    private boolean completed;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Client client;
    @ManyToOne
    private Workspace workspace;

    public Project(String title, String description, LocalDate start, LocalDate end) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
    }

    public void addAssignees(Collection<Participation> assignees) {
        this.assignees.addAll(assignees);
    }

    public void addAssignee(Participation participation) {
        addAssignees(Set.of(participation));
    }

    public void removeAssignee(Participation participation) {
        this.assignees.remove(participation);
    }

    public Status getStatus() {
        return computeStatus();
    }

    private Status computeStatus() {
        if (this.completed) return Status.DONE;

        if (this.start.isAfter(LocalDate.now())) {
            return Status.PLANNED;
        } else {
            return Status.ACTIVE;
        }
    }

    @Override
    public int compareTo(Project other) {
        Status status = getStatus();
        if (status == other.getStatus()) {
            return this.end.compareTo(other.end);
        }
        return this.getStatus().ordinal() - other.getStatus().ordinal();
    }

    public enum Status {
        ACTIVE, PLANNED, DONE
    }
}