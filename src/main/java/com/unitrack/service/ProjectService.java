package com.unitrack.service;

import com.unitrack.dto.request.ProjectDto;
import com.unitrack.entity.*;
import com.unitrack.repository.ClientRepository;
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
    private final ClientRepository clientRepository;

    public Project getByTitle(String title) {
        return projectRepository.findByTitle(title).orElse(null);
    }

    public Project getById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    public void add(ProjectDto dto) {
        Project project = new Project(dto.getTitle(), dto.getDescription(), dto.getStart(), dto.getDeadline());
        Set<Participation> assignees = dto.getAssignees().stream().filter(x->x.getId()!=null).map(x -> new Participation(collaboratorRepository.findById(x.getId()).orElseThrow(), project, Role.valueOf(x.getRole()))).collect(Collectors.toSet());
        project.addAssignees(assignees);
        project.setClient(clientRepository.findByName(dto.getClient()).orElse(new Client(dto.getClient())));
        projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    public Project update(Long id, ProjectDto dto) {
        Project project = projectRepository.findById(id).orElseThrow();
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setStart(dto.getStart());
        project.setEnd(dto.getDeadline());
        projectRepository.save(project);
        return project;
    }

    public List<Collaborator> getMembers(Long id) {
        Project project = projectRepository.findById(id).orElseThrow();
        return project.getAssignees().stream().map(Participation::getCollaborator).toList();
    }

}
