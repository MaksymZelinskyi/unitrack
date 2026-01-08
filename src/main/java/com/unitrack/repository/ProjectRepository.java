package com.unitrack.repository;

import com.unitrack.entity.Client;
import com.unitrack.entity.Project;
import com.unitrack.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByTitle(String title);

    int countByEndBetween(LocalDate start, LocalDate end);

    int countByEndBetweenAndWorkspace(LocalDate start, LocalDate end, Workspace workspace);

    List<Project> findAllByClient(Client client);

    List<Project> findAllByClientAndWorkspace(Client client, Workspace workspace);

    List<Project> findAllByWorkspace(Workspace workspace);

    int countByWorkspace(Workspace workspace);
}
