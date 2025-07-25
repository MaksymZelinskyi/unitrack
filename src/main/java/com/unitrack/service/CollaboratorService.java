package com.unitrack.service;

import com.unitrack.dto.CollaboratorDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Participation;
import com.unitrack.entity.Project;
import com.unitrack.entity.Skill;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final SkillRepository skillRepository;

    public void add(CollaboratorDto dto) {
        Collaborator collaborator = new Collaborator(dto.firstName(), dto.lastName(), dto.email(), dto.password());
        collaboratorRepository.save(collaborator);
    }

    public List<Collaborator> getAll() {
        return collaboratorRepository.findAll();
    }

    public Collaborator getById(Long id) {
        return collaboratorRepository.findById(id).orElse(null);
    }

    public Collaborator update(Long id, CollaboratorDto dto) {
        Collaborator collaborator = collaboratorRepository.findById(id).orElseThrow();
        collaborator.setEmail(dto.email());
        collaborator.setFirstName(dto.firstName());
        collaborator.setLastName(dto.lastName());
        collaborator.setPassword(dto.password());

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
}
