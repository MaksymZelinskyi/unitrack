package com.unitrack.controller;

import com.unitrack.dto.ProjectInListDto;
import com.unitrack.dto.request.CollaboratorDto;
import com.unitrack.entity.Skill;
import com.unitrack.service.CollaboratorService;
import com.unitrack.service.ProjectService;
import com.unitrack.service.SkillService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/collaborators")
@RequiredArgsConstructor
public class CollaboratorController extends AuthenticatedController {

    private final CollaboratorService collaboratorService;
    private final SkillService skillService;
    private final ProjectService projectService;

    @PostMapping("/")
    public void addCollaborator(CollaboratorDto collaborator) {
        collaboratorService.add(collaborator);
    }

    @DeleteMapping("/{id}")
    public String deleteCollaborator(@PathVariable Long id) {
        collaboratorService.delete(id);
        return "redirect:/home";
    }

    //todo: Add skill management
    @PostMapping("/{collaboratorId}/skills")
    public void addSkillToCollaborator(@PathVariable Long collaboratorId, Skill skill) {
        skillService.addCollaboratorSkill(collaboratorId, skill);
    }

    @DeleteMapping("/{collaboratorId}/skills")
    public void deleteCollaboratorSkill(@PathVariable Long collaboratorId, Skill skill) {
        skillService.deleteCollaboratorSkill(collaboratorId, skill);
    }

    @GetMapping("/new")
    public String newCollaborator(Model model) {
        List<Skill> skills = skillService.getAll();
        List<ProjectInListDto> projects = projectService.getAll().stream().map(x-> new ProjectInListDto(x.getId(), x.getTitle())).toList();
        model.addAttribute("selected", new ArrayList<>());
        model.addAttribute("collaboratorForm", new CollaboratorDto());
        model.addAttribute("skills", skills);
        model.addAttribute("projects", projects);
        return "new-collaborator";
    }

    @PostMapping("/new")
    public String newCollaborator(@Validated CollaboratorDto dto) {
        collaboratorService.add(dto);
        return "redirect:/home";
    }
}
