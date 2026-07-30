package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.*;
import com.unitrack.entity.*;
import com.unitrack.exception.AuthenticationException;
import com.unitrack.service.CollaboratorWorkspaceService;
import com.unitrack.service.ProjectService;
import com.unitrack.service.CollaboratorService;
import com.unitrack.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController extends AuthenticatedController {

    private final AuthorizationService authorizationService;
    private final CollaboratorWorkspaceService collaboratorWorkspaceService;

    @GetMapping
    public String root() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        List<WorkspaceDto> workspaces = collaboratorWorkspaceService.getWorkspaces(principal.getName());
        model.addAttribute("workspaces", workspaces);
        return "home";
    }

}