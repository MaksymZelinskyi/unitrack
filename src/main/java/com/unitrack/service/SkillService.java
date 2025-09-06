package com.unitrack.service;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Skill;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Not used yet
 */
@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final CollaboratorRepository collaboratorRepository;

    public List<Skill> getAll() {
        return skillRepository.findAll();
    }

    public void add(String skillName) {
        skillRepository.save(new Skill(skillName));
    }

    public void delete(Long id) {
        skillRepository.deleteById(id);
    }

    public void addCollaboratorSkill(Long id, Skill skill) {
        Collaborator collaborator = collaboratorRepository.findById(id).orElseThrow();
        if(!skillRepository.existsById(skill.getId())) throw new IllegalArgumentException();
        collaborator.addSkill(skill);
        collaboratorRepository.save(collaborator);
    }

    public void deleteCollaboratorSkill(Long id, Skill skill) {
        Collaborator collaborator = collaboratorRepository.findById(id).orElseThrow();
        if(!skillRepository.existsById(skill.getId())) throw new IllegalArgumentException();
        collaborator.deleteSkill(skill);

        collaboratorRepository.save(collaborator);
    }
}
