package com.unitrack.controller;


import com.unitrack.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController extends AuthenticatedController{

    private final StatisticsService statisticsService;

    @GetMapping
    public String getStats(Model model, Principal principal) {
        String userEmail = principal.getName();
        model.addAttribute("stats", statisticsService.getStats(userEmail));

        model.addAttribute("projectChart", statisticsService.getProjectChart(userEmail));
        model.addAttribute("userChart", statisticsService.getUserChart(userEmail));
        return "statistics";
    }
}