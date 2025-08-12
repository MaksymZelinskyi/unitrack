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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        collaboratorRepository.save(collaborator);
        mailService.sendCredentials(dto.getEmail(), dto.getPassword());
    }

    public List<Collaborator> getAll() {
        return collaboratorRepository.findAll();
    }

    public Collaborator getById(Long id) {
        return collaboratorRepository.findById(id).orElse(null);
    }

    public Collaborator update(String email, UpdateProfileDto dto) {
        Collaborator collaborator = collaboratorRepository.findByEmail(email).orElseThrow();
        collaborator.setEmail(dto.getEmail());
        collaborator.setFirstName(dto.getFirstName());
        collaborator.setLastName(dto.getLastName());
        collaborator.setPassword(passwordEncoder.encode(dto.getPassword()));
        collaborator.setAvatarUrl(dto.getAvatarUrl());

        return collaboratorRepository.save(collaborator);
    }

    public void delete(Long id) {
        collaboratorRepository.deleteById(id);
    }

    public List<Collaborator> searchBySkill(String skillName) {
        Skill skill = skillRepository.findByName(skillName).orElseThrow();
        return collaboratorRepository.findAllBySkillsContains(skill);
    }

    public List<Project> getProjects(Long id) {
        Collaborator collaborator = collaboratorRepository.findById(id).orElseThrow();
        return collaborator.getProjects().stream().map(Participation::getProject).toList();
    }

    public List<Project> getProjects(String email) {
        Collaborator collaborator = collaboratorRepository.findByEmail(email).orElseThrow();
        return collaborator.getProjects().stream().map(Participation::getProject).toList();
    }

    public Collaborator getByEmail(String email) {
        return collaboratorRepository.findByEmail(email).orElseThrow();
    }

    public List<Collaborator> getCollaboratorsByProject(Project project) {
        List<Participation> participations = participationRepository.findAllByProject(project);
        return participations.stream().map(Participation::getCollaborator).toList();
    }
}
