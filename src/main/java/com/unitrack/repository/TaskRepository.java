package com.unitrack.repository;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTitle(String title);

    Set<Task> findAllByAssigneesContains(Collaborator collaborator);
}
