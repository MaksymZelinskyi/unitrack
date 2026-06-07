package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.WorkspaceDto;
import com.unitrack.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final AuthorizationService authorizationService;
    private final WorkspaceService workspaceService;

    @PostMapping
    public String newWorkspace(String name, Principal principal) {
        return "";
    }

    @GetMapping("/{id}")
    public String getWorkspace(@PathVariable Long id, Principal principal) {
        return "";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authService.canDeleteWorkspace(#principal.getName(), #id)")
    public String deleteWorkspace(@PathVariable Long id, Principal principal)  {
        return "";
    }

    @GetMapping("/search")
    public Page<WorkspaceDto> searchWorkspace(@RequestParam String query,
                                              @RequestParam(name = "pagenumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(name = "pagesize", defaultValue = "1") int pageSize)  {
        return workspaceService.searchWorkspaces(query, Pageable.ofSize(pageSize).withPage(pageNumber));
    }
}
