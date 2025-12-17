package com.unitrack.service;

import com.unitrack.dto.request.CollaboratorDto;
import com.unitrack.dto.request.RegisterDto;
import com.unitrack.dto.request.UpdateProfileDto;
import com.unitrack.entity.*;
import com.unitrack.exception.CollaboratorNotFoundException;
import com.unitrack.exception.DuplicateException;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ParticipationRepository;
import com.unitrack.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final SkillRepository skillRepository;
    private final ParticipationRepository participationRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final GravatarService gravatarService;
    private final WorkspaceService workspaceService;

    public void add(CollaboratorDto dto, String currentUserEmail) {
        if (collaboratorRepository.existsByEmail(dto.getEmail()))
            throw new DuplicateException("A collaborator with email: " + dto.getEmail() + " already exists");

    public void add(CollaboratorDto dto) {
        Collaborator collaborator = new Collaborator(dto.getFirstName(), dto.getLastName(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()));
        collaborator.setAvatarUrl(gravatarService.getGravatarUrl(dto.getEmail(), 128));
        Workspace workspace = workspaceService.getUserWorkspace(currentUserEmail);
        Collaborator collaborator = new Collaborator(dto.getFirstName(), dto.getLastName(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()), workspace);

        collaborator = collaboratorRepository.save(collaborator);
        log.info("Collaborator with email {} has been added. Their id is {}", collaborator.getEmail(), collaborator.getId());
        mailService.sendCredentials(dto.getEmail(), dto.getPassword());
    }

    public void register(RegisterDto dto) {
        if (collaboratorRepository.existsByEmail(dto.email()))
            throw new DuplicateException("A user with email: " + dto.email() + " already exists");

        Workspace workspace = new Workspace(dto.teamName());
        Collaborator collaborator = new Collaborator(dto.firstName(), dto.lastName(), dto.email(), passwordEncoder.encode(dto.password()), workspace);
        collaborator.setAdmin(true);
        log.debug("Saving collaborator with email {}", collaborator.getEmail());
        collaborator = collaboratorRepository.save(collaborator);
        log.debug("Saved collaborator with id {}", collaborator.getId());
    }

    public List<Collaborator> getAll() {
        return collaboratorRepository.findAll();
    }

    public List<Collaborator> getAll(String userEmail) {
        Workspace workspace = workspaceService.getUserWorkspace(userEmail);
        return collaboratorRepository.findAllByWorkspace(workspace);
    }

    public Collaborator getById(Long id) {
        return collaboratorRepository.findById(id).orElse(null);
    }

    public Collaborator update(String email, UpdateProfileDto dto) {
        //retrieve the collaborator found by email(which should be unique)
        Collaborator collaborator = collaboratorRepository.findByEmail(email).orElseThrow(() -> new CollaboratorNotFoundException("email", email));

        //set data from the DTO
        collaborator.setEmail(dto.email());
        collaborator.setFirstName(dto.firstName());
        collaborator.setLastName(dto.lastName());
        collaborator.setPassword(passwordEncoder.encode(dto.password()));
        collaborator.setAvatarUrl(dto.avatarUrl());

        log.info("Collaborator with id {} is being updated", collaborator.getId());
        //persist
        return collaboratorRepository.save(collaborator);
    }

    public void delete(Long id) {
        log.info("Collaborator with id {} is being deleted", id);
        Collaborator collaborator = collaboratorRepository.findById(id).orElseThrow(() -> new CollaboratorNotFoundException("id", id));
        for(Participation p : collaborator.getProjects()) {
            p.getProject().removeAssignee(p);
        }
        collaborator.getProjects().clear();
        collaboratorRepository.delete(collaborator);
    }

    //not used
    public List<Collaborator> searchBySkill(String skillName) {
        Skill skill = skillRepository.findByName(skillName).orElseThrow();
        return collaboratorRepository.findAllBySkillsContains(skill);
    }

    //find by id
    public List<Project> getProjects(Long id) {
        Collaborator collaborator = collaboratorRepository.findById(id).orElseThrow(() -> new CollaboratorNotFoundException("id", id));
        log.debug("Collaborator with id {} fetched", id);
        return collaborator.getProjects().stream().map(Participation::getProject).toList();
    }

    //find by email
    public List<Project> getProjects(String email) {
        Collaborator collaborator = collaboratorRepository.findByEmail(email).orElseThrow(() -> new CollaboratorNotFoundException("email", email));
        log.debug("Collaborator with email {} fetched", email);
        return collaborator.getProjects().stream().map(Participation::getProject).toList();
    }

    public Collaborator getByEmail(String email) {
        return collaboratorRepository.findByEmail(email).orElseThrow(() -> new CollaboratorNotFoundException("email", email));
    }

    //find project assignees
    public List<Collaborator> getCollaboratorsByProject(Project project) {
        List<Participation> participations = participationRepository.findAllByProject(project);
        log.debug("Collaborators engaged in project {} fetched", project.getId());
        log.debug("Number of collaborators fetched: {}", participations != null ? participations.size() : 0);
        assert participations != null;
        return participations.stream().map(Participation::getCollaborator).toList();
    }
}
