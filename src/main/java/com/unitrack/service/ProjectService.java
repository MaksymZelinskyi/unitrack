package com.unitrack.service;

import com.unitrack.dto.request.ProjectDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Participation;
import com.unitrack.entity.Project;
import com.unitrack.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Project getByTitle(String title) {
        return projectRepository.findByTitle(title).orElse(null);
    }

    public Project getById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public void add(ProjectDto dto) {
        Project project = new Project(dto.title(), dto.description(), dto.start(), dto.end(), Project.Status.valueOf(dto.status()));
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
        project.setEnd(dto.end());
        project.setStatus(Project.Status.valueOf(dto.status()));
        projectRepository.save(project);
        return project;
    }

    public List<Collaborator> getMembers(Long id) {
        Project project = projectRepository.findById(id).orElseThrow();
        return project.getAssignees().stream().map(Participation::getCollaborator).toList();
    }
}
