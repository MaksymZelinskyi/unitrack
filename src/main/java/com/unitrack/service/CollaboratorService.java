package com.unitrack.service;

import com.unitrack.dto.request.CollaboratorDto;
import com.unitrack.dto.request.UpdateProfileDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Participation;
import com.unitrack.entity.Project;
import com.unitrack.entity.Skill;
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

    public void add(CollaboratorDto dto) {
        Collaborator collaborator = new Collaborator(dto.getFirstName(), dto.getLastName(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()));
        collaborator = collaboratorRepository.save(collaborator);
        log.info("Collaborator with email {} has been added. Their id is {}", collaborator.getEmail(), collaborator.getId());
        mailService.sendCredentials(dto.getEmail(), dto.getPassword());
    }

    public List<Collaborator> getAll() {
        return collaboratorRepository.findAll();
    }

    public Collaborator getById(Long id) {
        return collaboratorRepository.findById(id).orElse(null);
    }

    public Collaborator update(String email, UpdateProfileDto dto) {
        //retrieve the collaborator found by email(which should be unique)
        Collaborator collaborator = collaboratorRepository.findByEmail(email).orElseThrow();

        //set data from the DTO
        collaborator.setEmail(dto.getEmail());
        collaborator.setFirstName(dto.getFirstName());
        collaborator.setLastName(dto.getLastName());
        collaborator.setPassword(passwordEncoder.encode(dto.getPassword()));
        collaborator.setAvatarUrl(dto.getAvatarUrl());

        log.info("Collaborator with id {} is being updated", collaborator.getId());
        //persist
        return collaboratorRepository.save(collaborator);
    }

    public void delete(Long id) {
        log.info("Collaborator with id {} is being deleted", id);
        Collaborator collaborator = collaboratorRepository.findById(id).orElseThrow();
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
        Collaborator collaborator = collaboratorRepository.findById(id).orElseThrow();
        log.debug("Collaborator with id {} fetched", id);
        return collaborator.getProjects().stream().map(Participation::getProject).toList();
    }

    //find by email
    public List<Project> getProjects(String email) {
        Collaborator collaborator = collaboratorRepository.findByEmail(email).orElseThrow();
        log.debug("Collaborator with email {} fetched", email);
        return collaborator.getProjects().stream().map(Participation::getProject).toList();
    }

    public Collaborator getByEmail(String email) {
        return collaboratorRepository.findByEmail(email).orElseThrow();
    }

    //find project assignees
    public List<Collaborator> getCollaboratorsByProject(Project project) {
        List<Participation> participations = participationRepository.findAllByProject(project);
        log.debug("Collaborators engaged in project {} fetched", project.getId());
        log.debug("Number of collaborators fetched: {}", participations != null ? participations.size() : 0);
        return participations.stream().map(Participation::getCollaborator).toList();
    }
}
