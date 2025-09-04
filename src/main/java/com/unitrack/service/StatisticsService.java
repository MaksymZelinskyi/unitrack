package com.unitrack.service;

import com.unitrack.dto.*;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Project;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;
import com.unitrack.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final ProjectRepository projectRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final TaskRepository taskRepository;

    public StatisticsDto getStats() {
        StatisticsDto dto = new StatisticsDto();
        ProjectStatistics projectStats = new ProjectStatistics();
        TaskStatistics taskStats = new TaskStatistics();

        dto.setProjects(projectStats);
        dto.setTasks(taskStats);

        projectStats.setDeadlines(projectRepository.countByEndBetween(LocalDate.now().minusMonths(1), LocalDate.now()));
        projectStats.setTotal((int)projectRepository.count());

        taskStats.setTotal((int)taskRepository.count());
        taskStats.setDeadlines(taskRepository.countByCompletedOnAfter(LocalDate.now().minusMonths(1)));
        //todo: add status management
        return dto;
    }

    /**
     *
     * @return Data for the chart of tasks completed last month per project
     */
    public ProjectStatsChart getProjectChart() {
        ProjectStatsChart chart = new ProjectStatsChart();
        List<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            chart.addProject(project.getTitle(), (int) project.getTasks().stream().filter(x -> x.getCompletedOn() == null || x.getCompletedOn().isAfter(LocalDate.now().minusMonths(1))).count());
        }
        return chart;
    }

    /**
     *
     * @return Data for the chart of tasks completed last month per collaborator
     */
    public UserStatsChart getUserChart() {
        UserStatsChart chart = new UserStatsChart();
        List<Collaborator> collaborators = collaboratorRepository.findAll();
        for (Collaborator collaborator : collaborators) {
            chart.addUser(collaborator.getFirstName() + " " +collaborator.getLastName().charAt(0), (int)collaborator.getTasks().stream().filter(x -> x.getCompletedOn().isAfter(LocalDate.now().minusMonths(1))).count());
        }
        return chart;
    }
}
