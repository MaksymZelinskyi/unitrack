package com.unitrack.service;

import com.unitrack.dto.request.AssignmentDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Participation;
import com.unitrack.entity.Project;
import com.unitrack.entity.Role;
import com.unitrack.exception.AuthenticationException;
import com.unitrack.exception.CollaboratorNotFoundException;
import com.unitrack.exception.EntityNotFoundException;
import com.unitrack.exception.ProjectNotFoundException;
import com.unitrack.repository.AssignmentRepository;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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
        Collaborator collaborator = collaboratorRepository.findById(dto.collaboratorId()).orElseThrow(() -> new CollaboratorNotFoundException("id", dto.collaboratorId()));
        //extract project
        Project project = projectRepository.findById(dto.projectId()).orElseThrow(() -> new ProjectNotFoundException("id", dto.projectId()));
        //add project into the list of collaborator's projects
        Participation participation = new Participation(collaborator, project, Role.valueOf(dto.role()));
        collaborator.addProject(participation);
        //save changes(cascade)
        project.addAssignee(participation);
        collaboratorRepository.save(collaborator);
    }

    public void remove(Long id) {
        assignmentRepository.deleteById(id);
    }

}
