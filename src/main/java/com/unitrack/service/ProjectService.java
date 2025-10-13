package com.unitrack.service;

import com.unitrack.dto.request.ProjectDto;
import com.unitrack.dto.request.UpdateProjectDto;
import com.unitrack.entity.*;
import com.unitrack.repository.ClientRepository;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ParticipationRepository;
import com.unitrack.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final ClientRepository clientRepository;
    private final ParticipationRepository participationRepository;

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
        //create project entity with dto data
        Project project = new Project(dto.getTitle(), dto.getDescription(), dto.getStart(), dto.getDeadline());

        //set project assignees
        Set<Participation> assignees = dto.getAssignees()
                .stream()
                .filter(x -> x.getId() != null)
                .map(x -> new Participation(
                        collaboratorRepository.findById(x.getId()).orElseThrow(),
                        project,
                        Role.valueOf(x.getRole())
                ))
                .collect(Collectors.toSet());
        project.addAssignees(assignees);
        project.setClient(clientRepository.findByName(dto.getClient()).orElse(new Client(dto.getClient())));
        //set default status
        if (dto.getStart().isAfter(LocalDate.now()))
            project.setStatus(Project.Status.ACTIVE); //started
        else
            project.setStatus(Project.Status.PLANNED); //planned

        projectRepository.save(project); //save
        log.info("Project '{}' saved", project.getTitle());
    }

    public void delete(Long id) {
        log.info("Project '{}' deleted", id);
        projectRepository.deleteById(id);
    }

    public Project update(Long id, UpdateProjectDto dto) {
        //extract existing
        Project project = projectRepository.findById(id).orElseThrow();
        log.debug("Project with id {} fetched", id);
        //set DTO data
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setStart(dto.getStart());
        project.setEnd(dto.getDeadline());

        Set<Participation> assignees = dto.getAssignees()
                .stream()
                .filter(x -> x.getId() != null)
                .map(x -> new Participation(collaboratorRepository.findById(x.getId()).orElseThrow(), project, Role.valueOf(x.getRole())))
                .collect(Collectors.toSet());
        project.setAssignees(assignees);
        log.debug("Number of project assignees added: {}", assignees.size());
        //persist changes
        projectRepository.save(project);
        return project;
    }

    public List<Collaborator> getMembers(Long id) {
        Project project = projectRepository.findById(id).orElseThrow();
        var members = project.getAssignees().stream().map(Participation::getCollaborator).toList();
        log.info("Assignees fetched for project {}", id);
        log.debug("Number of project assignees: {}", members.size());
        return members;
    }

    public List<Participation> getProjectAssignees(Project project) {
        return participationRepository.findAllByProject(project);
    }

    public void markAsCompleted(Long id, boolean completed) {
        Project project = projectRepository.findById(id).orElseThrow();
        log.debug("Project {} fetched", id);
        var status = Project.Status.DONE;
        if (!completed) {
            status = project.getStart().isAfter(LocalDate.now()) ? Project.Status.PLANNED : Project.Status.ACTIVE;
        }
        project.setStatus(status);

        log.info("Project {} is being marked as {}", id, status.name());
        projectRepository.save(project);
    }
}
