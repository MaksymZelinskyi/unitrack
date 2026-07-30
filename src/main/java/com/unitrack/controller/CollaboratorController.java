package com.unitrack.controller;

import com.unitrack.dto.*;
import com.unitrack.dto.request.CollaboratorDto;
import com.unitrack.entity.*;
import com.unitrack.service.CollaboratorService;
import com.unitrack.service.ProjectService;
import com.unitrack.service.SkillService;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Controller
@RequestMapping("/collaborators")
@RequiredArgsConstructor
public class CollaboratorController extends AuthenticatedController {

    private static final Logger log = LoggerFactory.getLogger(CollaboratorController.class);
    private final CollaboratorService collaboratorService;
    private final SkillService skillService;
    private final ProjectService projectService;


    @DeleteMapping("/{id}")
    public String deleteCollaborator(@PathVariable Long id) {
        collaboratorService.delete(id);
        return "redirect:/home";
    }

    @GetMapping("/new")
    public String newCollaborator(Model model, Principal principal) {
        List<Skill> skills = skillService.getAll();
        List<ProjectInListDto> projects = projectService.getAllSorted(principal.getName())
                .stream()
                .map(x-> new ProjectInListDto(x.getId(), x.getTitle()))
                .sorted(Comparator.comparing(ProjectInListDto::title))
                .toList();
        model.addAttribute("selected", new ArrayList<>());
        model.addAttribute("collaboratorForm", new CollaboratorDto());
        model.addAttribute("skills", skills);
        model.addAttribute("projects", projects);
        return "new-collaborator";
    }

    @PostMapping("/new")
    public String newCollaborator(@Validated CollaboratorDto dto, Principal principal) {
        collaboratorService.add(dto, principal.getName());
        return "redirect:/home";
    }

    @GetMapping("/{id}")
    public String getCollaborator(@PathVariable Long id, Model model) {
        Collaborator collaborator = collaboratorService.getById(id);

        DisplayCollaboratorDto dto = new DisplayCollaboratorDto(collaborator.getFullName(), collaborator.getEmail(), collaborator.getAvatarUrl());

        List<CollaboratorWorkspaceDto> workspaces = collaborator.getWorkspaces()
                .stream()
                .map(cw -> {
                    Workspace workspace = cw.getWorkspace();
                    return new CollaboratorWorkspaceDto(
                            workspace.getId(), workspace.getName(), workspace.getDescription()
                    );
                })
                .toList();
        List<String> skills = collaborator.getSkills().stream().map(Skill::getName).toList();

        log.debug("Workspaces fetched for collaborator {}: {}", collaborator.getFullName(), workspaces.size());

        model.addAttribute("workspaces", workspaces);
        model.addAttribute("collaborator", dto);
        model.addAttribute("skills", skills);
        return "collaborator";
    }

}
