package com.unitrack.controller;

import com.unitrack.dto.CollaboratorProjectDto;
import com.unitrack.dto.DisplayCollaboratorDto;
import com.unitrack.dto.ProjectInListDto;
import com.unitrack.dto.TaskInListDto;
import com.unitrack.dto.request.CollaboratorDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Participation;
import com.unitrack.entity.Skill;
import com.unitrack.entity.Task;
import com.unitrack.service.CollaboratorService;
import com.unitrack.service.ProjectService;
import com.unitrack.service.SkillService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
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
        List<ProjectInListDto> projects = projectService.getAll()
                .stream()
                .map(x-> new ProjectInListDto(x.getId(), x.getTitle()))
                .sorted(Comparator.comparing(x -> x.getTitle()))
                .toList();
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

    @GetMapping("/{id}")
    public String getCollaborator(@PathVariable Long id, Model model) {
        Collaborator collaborator = collaboratorService.getById(id);

        DisplayCollaboratorDto dto = new DisplayCollaboratorDto();
        dto.setFullName(collaborator.getFullName());
        dto.setEmail(collaborator.getEmail());
        dto.setAvatarUrl(collaborator.getAvatarUrl());
        List<CollaboratorProjectDto> projects = new ArrayList<>();
        for (Participation p : collaborator.getProjects()) {
            CollaboratorProjectDto projectDto = new CollaboratorProjectDto();
            projectDto.setProjectId(p.getProject().getId());
            projectDto.setTitle(p.getProject().getTitle());
            projectDto.setRole(String.valueOf(p.getRoles().stream().findFirst().orElse(null)));
            projectDto.setTasks(p.getProject().getTasks()
                    .stream()
                    .map(x -> new TaskInListDto(x.getId(), x.getTitle(), x.getDescription(),
                            x.getDeadline(), x.getStatus()== Task.Status.DONE))
                    .toList()
            );
            projects.add(projectDto);
        }
        model.addAttribute("projects", projects);
        model.addAttribute("collaborator", dto);
        return "collaborator";
    }
}
