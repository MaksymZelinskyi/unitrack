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

    public void add(AssignmentDto dto) {
        Collaborator collaborator = collaboratorRepository.findById(dto.collaboratorId()).orElseThrow();
        Project project = projectRepository.findById(dto.projectId()).orElseThrow();
        collaborator.addProject(project, Role.valueOf(dto.role()));

        collaboratorRepository.save(collaborator);
    }

    public void remove(Long id) {
        assignmentRepository.deleteById(id);
    }

}
