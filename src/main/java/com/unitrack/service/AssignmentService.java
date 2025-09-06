package com.unitrack.service;

import com.unitrack.dto.request.AssignmentDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Project;
import com.unitrack.entity.Role;
import com.unitrack.repository.AssignmentRepository;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final CollaboratorRepository collaboratorRepository;
    private final ProjectRepository projectRepository;
    private final AssignmentRepository assignmentRepository;

    /**
     * Adds a project-collaborator relation based on the parameter and persists it
     * @param dto - project id, collaborator id and the role of the collaborator
     */
    public void add(AssignmentDto dto) {
        //extract assignee
        Collaborator collaborator = collaboratorRepository.findById(dto.collaboratorId()).orElseThrow();
        //extract project
        Project project = projectRepository.findById(dto.projectId()).orElseThrow();
        //add project into the list of collaborator's projects
        collaborator.addProject(project, Role.valueOf(dto.role()));
        //save changes(cascade)
        collaboratorRepository.save(collaborator);
    }

    public void remove(Long id) {
        assignmentRepository.deleteById(id);
    }

}
