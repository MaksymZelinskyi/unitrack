package com.unitrack.controller;

import com.unitrack.entity.Project;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;
import com.unitrack.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController extends AuthenticatedController{

    private final StatisticsService statisticsService;
    private final CollaboratorRepository collaboratorRepository;
    private final ProjectRepository projectRepository;

    @GetMapping
    public String getStats(Model model) {
        model.addAttribute("stats", statisticsService.getStats());
        model.addAttribute("usernames", collaboratorRepository.findAll().stream().map(x -> x.getFirstName() + " " + x.getLastName().charAt(0)));
        model.addAttribute("usernames", collaboratorRepository.findAll().stream().map(x -> x.getFirstName() + " " + x.getLastName().charAt(0)));
        model.addAttribute("users", projectRepository.findAll().stream().map(Project::getTitle));

        model.addAttribute("projectChart", statisticsService.getProjectChart());
        model.addAttribute("userChart", statisticsService.getUserChart());
        return "statistics";
    }
}