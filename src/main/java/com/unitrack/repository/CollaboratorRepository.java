package com.unitrack.repository;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {

    List<Collaborator> findAllBySkill(Skill skill);
}
