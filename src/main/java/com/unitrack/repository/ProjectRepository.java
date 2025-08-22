package com.unitrack.repository;

import com.unitrack.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByTitle(String title);

    int countByEndBetween(LocalDate start, LocalDate end);

}
