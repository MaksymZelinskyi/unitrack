package com.unitrack.service;

import com.unitrack.dto.request.ProjectDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Participation;
import com.unitrack.entity.Project;
import com.unitrack.entity.Role;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CollaboratorRepository collaboratorRepository;

    public Project getByTitle(String title) {
        return projectRepository.findByTitle(title).orElse(null);
    }

    public Project getById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public void add(ProjectDto dto) {
        Project project = new Project(dto.title(), dto.description(), dto.start(), dto.deadline());
        Set<Participation> assignees = dto.assignees().stream().map(x -> new Participation(collaboratorRepository.findById(x.id()).orElseThrow(), project, Role.valueOf(x.role()))).collect(Collectors.toSet());
       project.setAssignees(assignees);
        projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    public Project update(Long id, ProjectDto dto) {
        Project project = projectRepository.findById(id).orElseThrow();
        project.setTitle(dto.title());
        project.setDescription(dto.description());
        project.setStart(dto.start());
        project.setEnd(dto.deadline());
        projectRepository.save(project);
        return project;
    }

    public List<Collaborator> getMembers(Long id) {
        Project project = projectRepository.findById(id).orElseThrow();
        return project.getAssignees().stream().map(Participation::getCollaborator).toList();
    }
}
