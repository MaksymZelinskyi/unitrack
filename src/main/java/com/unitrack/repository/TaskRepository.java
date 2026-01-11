package com.unitrack.repository;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Project;
import com.unitrack.entity.Task;
import com.unitrack.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTitle(String title);

    Set<Task> findAllByAssigneesContains(Collaborator collaborator);

    Set<Task> findAllByWorkspaceAndAssigneesContains(Workspace workspace, Collaborator collaborator);

    Set<Task> findAllByProject(Project project);

    Set<Task> findAllByCompletedOnAfter(LocalDate date);

    Set<Task> findAllByCompletedOnAfterAndWorkspace(LocalDate date, Workspace workspace);

    int countByCompletedOnAfter(LocalDate date);

    int countByCompletedOnAfterAndWorkspace(LocalDate date, Workspace workspace);

    int countByWorkspace(Workspace workspace);
}
