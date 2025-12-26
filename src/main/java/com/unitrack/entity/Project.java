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

    private Status status;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Client client;

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

    @Override
    public int compareTo(Project other) {
        if (this.status == Status.DONE && other.status != Status.DONE) {
            return 1;
        }
        return this.end.compareTo(other.end);
    }

    public enum Status {
        PLANNED, ACTIVE, DONE
    }
}