package com.unitrack.controller;

import com.unitrack.dto.CollaboratorInListDto;
import com.unitrack.dto.WorkspaceDto;
import com.unitrack.entity.Skill;
import com.unitrack.service.CollaboratorService;
import com.unitrack.service.SkillService;
import com.unitrack.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final WorkspaceService workspaceService;
    private final CollaboratorService collaboratorService;
    private final SkillService skillService;

    @GetMapping("/workspace")
    public Page<WorkspaceDto> searchWorkspace(String query,
                                              @RequestParam(name = "pagenumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(name = "pagesize", defaultValue = "5") int pageSize)  {
        return workspaceService.searchWorkspaces(query, Pageable.ofSize(pageSize).withPage(pageNumber));
    }

    @GetMapping("/collaborator")
    public Page<CollaboratorInListDto> searchCollaborator(String query,
                                                       @RequestParam(name = "pagenumber", defaultValue = "0") int pageNumber,
                                                       @RequestParam(name = "pagesize", defaultValue = "5") int pageSize) {
        return collaboratorService.searchCollab(query, Pageable.ofSize(pageSize).withPage(pageNumber));
    }


    @GetMapping("/skill")
    public List<Skill> searchSkills(String searchQuery) {
        return skillService.searchSkill(searchQuery);
    }
}
