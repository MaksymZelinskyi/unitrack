package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"project", "assignees"})
@ToString(exclude = {"assignees", "comments"})
public class Task implements Comparable<Task> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private LocalDate completedOn;
    @Enumerated
    private Status status;

    @ManyToMany
    private Set<Collaborator> assignees = new HashSet<>();
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    private Project project;
    @ManyToOne
    private Workspace workspace;

    public Task(String title, String description, LocalDateTime deadline, Project project) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.project = project;
    }

    public void addAssignees(Collection<Collaborator> assignees) {
        this.assignees.addAll(assignees);
    }

    @Override
    public int compareTo(Task other) {
        if (this.status == Status.DONE && other.status != Task.Status.DONE) {
            return 1;
        }
        return this.deadline.compareTo(other.deadline);
    }


    public enum Status {
        TODO, IN_PROGRESS, DONE
    }
}
