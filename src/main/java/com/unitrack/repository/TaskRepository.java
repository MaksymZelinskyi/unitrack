package com.unitrack.repository;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Project;
import com.unitrack.entity.Task;
import com.unitrack.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTitle(String title);

    Set<Task> findAllByProject(Project project);

    int countByCompletedOnAfter(LocalDate date);


    int countByWorkspace(Workspace workspace);

    int countByStatusAndWorkspace(Task.Status status, Workspace workspace);

    //in-time tasks
    @Query("select count(t) from Task t where t.status = :status and t.completedOn <= function('DATE', t.deadline) and t.workspace = :workspace")
    int countByStatusAndCompletedOnIsBeforeDeadlineAndWorkspace(Task.Status status, Workspace workspace);

    int countByStatusNotAndDeadlineBetweenAndWorkspace(Task.Status status, LocalDateTime start, LocalDateTime end, Workspace workspace);

    //overdue tasks
    int countByStatusNotAndDeadlineBeforeAndWorkspace(Task.Status status, LocalDateTime deadline, Workspace workspace);

}
