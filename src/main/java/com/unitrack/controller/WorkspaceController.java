package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private AuthorizationService authorizationService;

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
}
