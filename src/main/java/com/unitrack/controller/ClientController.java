package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.ClientProjectDto;
import com.unitrack.dto.request.UpdateClientDto;
import com.unitrack.service.ClientService;
import com.unitrack.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ProjectService projectService;
    private final AuthorizationService authService;

    @GetMapping("/{id}")
    public String getClient(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.getById(id));
        List<ClientProjectDto> projects = projectService.getAllByClient(id).stream().map(x -> new ClientProjectDto(x.getId(), x.getTitle())).toList();
        model.addAttribute("projects", projects);
        return "client";
    }

    @GetMapping("/update/{id}")
    public String updateClient(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.getById(id));
        List<ClientProjectDto> projects = projectService.getAllByClient(id).stream().map(x -> new ClientProjectDto(x.getId(), x.getTitle())).toList();
        model.addAttribute("projects", projects);
        return "client";
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authService.isAdmin(#principal.getName())")
    public String updateProject(@PathVariable Long id, @ModelAttribute("clientForm") @Validated UpdateClientDto project, Principal principal) {
        clientService.updateById(id, project);
        return "redirect:" + id;
    }
}
