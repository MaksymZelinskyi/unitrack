package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.ClientDto;
import com.unitrack.dto.ClientProjectDto;
import com.unitrack.dto.ProjectClientDto;
import com.unitrack.dto.ProjectDto;
import com.unitrack.dto.request.UpdateClientDto;
import com.unitrack.entity.Client;
import com.unitrack.entity.Project;
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
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController extends AuthenticatedController {

    private final ClientService clientService;
    private final ProjectService projectService;
    private final AuthorizationService authService;

    @GetMapping("/{id}")
    public String getClient(@PathVariable Long id, Model model) {
        Client client =  clientService.getById(id);
        model.addAttribute("client", new ClientDto(client.getId(), client.getName(), client.getEmail(), client.getPhoneNumber()));
        List<Project> projects = projectService.getAllByClient(id);
        model.addAttribute("projects", projects
                .stream()
                .map(x -> new ClientProjectDto(x.getId(), x.getTitle(), x.getDescription(),
                        x.getStart(), x.getEnd(), x.getStatus().name()))
                .toList());
        return "client";
    }

    @GetMapping("/update/{id}")
    public String updateClient(@PathVariable Long id, Model model) {
        Client client = clientService.getById(id);
        model.addAttribute("client", new ClientDto(client.getId(), client.getName(), client.getEmail(), client.getPhoneNumber()));
        return "update-client";
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authService.isAdmin(#principal.getName())")
    public String updateProject(@PathVariable Long id, @ModelAttribute("clientForm") @Validated UpdateClientDto clientDto, Principal principal) {
        clientService.updateById(id, clientDto);
        return "redirect:" + id;
    }
}
