package com.unitrack.controller;

import com.unitrack.dto.CollaboratorDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Project;
import com.unitrack.service.CollaboratorService;

import com.unitrack.service.ProjectService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/collaborators")
@RequiredArgsConstructor
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    @PostMapping("/")
    public void addCollaborator(CollaboratorDto collaborator) {
        collaboratorService.add(collaborator);
    }

    @PutMapping("/{id}")
    public Collaborator modifyCollaborator(@PathVariable Long id, CollaboratorDto collaborator) {
        return collaboratorService.update(id, collaborator);
    }

    @GetMapping("/")
    public List<Collaborator> getAllCollaborators() {
        return collaboratorService.getAll();
    }

    @DeleteMapping("/{id}")
    public void deleteCollaborator(@PathVariable Long id) {
        collaboratorService.delete(id);
    }

    @GetMapping("/search")
    public List<Collaborator> searchBySkill(@RequestParam String skill) {
        return collaboratorService.searchBySkill(skill);
    }

    @GetMapping("/{id}/projects")
    public List<Project> getCollaboratorProjects(@PathVariable Long id) {
        return collaboratorService.getProjects(id);
    }
}
