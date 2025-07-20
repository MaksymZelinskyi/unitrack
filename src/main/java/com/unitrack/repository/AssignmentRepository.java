package com.unitrack.repository;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Participation;
import com.unitrack.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Participation, Long> {

    Participation findByProjectAndCollaborator(Project project, Collaborator collaborator);
}
