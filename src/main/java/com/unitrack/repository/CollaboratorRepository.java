package com.unitrack.repository;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Skill;
import com.unitrack.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {

    List<Collaborator> findAllBySkillsContains(Skill skill);

    Optional<Collaborator> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select cw.collaborator from CollaboratorWorkspace cw where cw.workspace = :workspace")
    List<Collaborator> findAllByWorkspace(Workspace workspace);

    @Query("SELECT c FROM Collaborator c WHERE " +
            "UPPER(CONCAT(c.firstName, ' ', c.lastName)) LIKE CONCAT('%', UPPER(:query), '%') " +
            "OR UPPER(c.firstName) LIKE CONCAT('%', UPPER(:query), '%') " +
            "OR UPPER(c.lastName) LIKE CONCAT('%', UPPER(:query), '%')")
    Page<Collaborator> findByFirstNameAndLastNameContainingIgnoreCase(
            @Param("query") String query,
            Pageable pageable
    );
}
