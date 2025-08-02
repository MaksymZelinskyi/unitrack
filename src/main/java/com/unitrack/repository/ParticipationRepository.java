package com.unitrack.repository;

import com.unitrack.entity.Participation;
import com.unitrack.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    List<Participation> findAllByProject(Project project);
}
