package com.unitrack.controller;


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

    @GetMapping
    public String getStats(Model model) {
        model.addAttribute("stats", statisticsService.getStats());

        model.addAttribute("projectChart", statisticsService.getProjectChart());
        model.addAttribute("userChart", statisticsService.getUserChart());
        return "statistics";
    }
}