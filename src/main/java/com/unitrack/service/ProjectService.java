package com.unitrack.service;

import com.unitrack.dto.request.AssigneeDto;
import com.unitrack.dto.request.ProjectDto;
import com.unitrack.dto.request.UpdateProjectDto;
import com.unitrack.entity.*;
import com.unitrack.exception.*;
import com.unitrack.repository.ClientRepository;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ParticipationRepository;
import com.unitrack.repository.ProjectRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
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
    private final ClientService clientService;
    private final ParticipationRepository participationRepository;
    private final WorkspaceService workspaceService;

    public Project getByTitle(String title) {
        return projectRepository.findByTitle(title).orElse(null);
    }

    public Project getById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    public void add(ProjectDto dto, String userEmail) {
        if (dto.getStart().isAfter(dto.getDeadline()))
            throw new ValidationException("Project start time must precede the deadline");

        Workspace workspace = workspaceService.getUserWorkspace(userEmail);

        //create project entity with dto data
        Project project = new Project(dto.getTitle(), dto.getDescription(), dto.getStart(), dto.getDeadline(), workspace);

        //set project assignees
        Set<Participation> assignees = dto.getAssignees()
                .stream()
                .filter(x -> x.getId() != null)
                .map(x -> new Participation(
                        collaboratorRepository.findById(x.getId()).orElseThrow(() -> new CollaboratorNotFoundException("id", x.getId())),
                        project,
                        Role.valueOf(x.getRole())
                ))
                .collect(Collectors.toSet());
        project.addAssignees(assignees);
        if (dto.getNewClient() != null && !dto.getNewClient().isBlank()) {
            project.setClient(clientService.getByNameOrCreate(dto.getNewClient(), userEmail));
        } else  if (dto.getClient() != null && !dto.getClient().isBlank()) {
            project.setClient(clientService.getByNameOrCreate(dto.getClient(), userEmail));
        }

        projectRepository.save(project); //save
        log.info("Project '{}' saved", project.getTitle());
    }

    public void delete(Long id) {
        log.info("Project '{}' deleted", id);
        projectRepository.deleteById(id);
    }

    public Project update(Long id, UpdateProjectDto dto, String userEmail) {
        //extract existing
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException("id", id));
        log.debug("Project with id {} fetched", id);
        //set DTO data
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());

        if (dto.getStart().isAfter(dto.getDeadline()))
            throw new ValidationException("Project start time must precede the deadline");

        project.setStart(dto.getStart());
        project.setEnd(dto.getDeadline());

        if (dto.getNewClient() != null && !dto.getNewClient().isBlank()) {
            project.setClient(clientService.getByNameOrCreate(dto.getNewClient(), userEmail));
        } else  if (dto.getClient() != null && !dto.getClient().isBlank()) {
            project.setClient(clientService.getByNameOrCreate(dto.getClient(), userEmail));
        }

        log.debug("Assignees set for project: {}", dto.getAssignees().size());
        log.debug("Assignee id + role: {}", dto.getAssignees().stream().map(x -> "Id: " + x.getId() + "; role: " + x.getRole()).toList());
        Set<Participation> assignees = dto.getAssignees()
                .stream()
                .filter(x -> x.getId() != null)
                .map(x -> {
                    Collaborator collaborator = collaboratorRepository.findById(x.getId()).orElseThrow(() -> new CollaboratorNotFoundException("id", x.getId()));
                    if (collaborator.getWorkspace() != project.getWorkspace()) throw new WorkspaceException("Collaborator isn't in the project's workspace");
                    return new Participation(collaborator, project, Role.valueOf(x.getRole().split(",")[0]));
                    }
                )
                .collect(Collectors.toSet());
        project.getAssignees().clear();
        project.addAssignees(assignees);
        log.debug("Number of project assignees added: {}", assignees.size());

        //persist changes
        projectRepository.save(project);
        return project;
    }

    public List<Collaborator> getMembers(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException("id", id));
        var members = project.getAssignees().stream().map(Participation::getCollaborator).toList();
        log.info("Assignees fetched for project {}", id);
        log.debug("Number of project assignees: {}", members.size());
        return members;
    }

    public List<Participation> getProjectAssignees(Project project) {
        List<Participation> participations = participationRepository.findAllByProject(project);
        log.debug("Participations fetched for project with id {}: {}", project.getId(), participations);
        return participations;
    }

    public void markAsCompleted(Long id, boolean completed) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException("id", id));
        log.debug("Project {} fetched", id);
        project.setCompleted(completed);

        log.info("Project {} is being marked as {}", id, project.getStatus().name());
        projectRepository.save(project);
    }

    public List<Project> getAllSorted(String userEmail) {
        Workspace workspace = workspaceService.getUserWorkspace(userEmail);
        List<Project> projects = projectRepository.findAllByWorkspace(workspace);
        projects.sort(Comparator.naturalOrder());
        return projects;
    }

    public List<Project> getAllByClient(Long clientId) {
        Client client = clientService.getById(clientId);
        return projectRepository.findAllByClient(client);
    }
}