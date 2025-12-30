package com.unitrack.repository;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Skill;
import com.unitrack.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {

    List<Collaborator> findAllBySkillsContains(Skill skill);

    Optional<Collaborator> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Collaborator> findAllByWorkspace(Workspace workspace);
}
