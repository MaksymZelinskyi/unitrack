package com.unitrack.service;

import com.unitrack.dto.*;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Project;
import com.unitrack.entity.Task;
import com.unitrack.entity.Workspace;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;
import com.unitrack.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final ProjectRepository projectRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final TaskRepository taskRepository;
    private final WorkspaceService workspaceService;

    public StatisticsDto getStats(String userEmail) {
        Workspace workspace = workspaceService.getUserWorkspace(userEmail);
        StatisticsDto dto = new StatisticsDto();
        ProjectStatisticsDto projectStats = new ProjectStatisticsDto();
        TaskStatistics taskStats = new TaskStatistics();

        projectStats.setDeadlines(
                projectRepository
                        .countByEndBetweenAndWorkspace(
                                LocalDate.now().minusMonths(1), LocalDate.now(), workspace
                        )
        );
        projectStats.setTotal(projectRepository.countByWorkspace(workspace));

        taskStats.setTotal(taskRepository.countByWorkspace(workspace));
        taskStats.setDeadlines(taskRepository.countByCompletedOnAfterAndWorkspace(LocalDate.now().minusMonths(1), workspace));

        dto.setProjects(projectStats);
        dto.setTasks(taskStats);

        log.info("Project/task statistics generated");
        return dto;
    }

    /**
     * getProjectChart
     * @return Data for the chart of tasks completed last month per project
     */
    public ProjectStatsChart getProjectChart(String userEmail) {
        Workspace workspace = workspaceService.getUserWorkspace(userEmail);
        ProjectStatsChart chart = new ProjectStatsChart();
        List<Project> projects = projectRepository.findAllByWorkspace(workspace);

        for (Project project : projects) {
            chart.addProject(project.getTitle(), (int) project.getTasks()
                    .stream()
                    .filter(x -> x.getStatus() == Task.Status.DONE && x.getCompletedOn() != null && x.getCompletedOn().isAfter(LocalDate.now().minusMonths(1)))
                    .count());
        }
        log.info("Project statistics chart generated");
        return chart;
    }

    /**
     * getUserChart
     * @return Data for the chart of tasks completed last month per collaborator
     */
    public UserStatsChart getUserChart(String userEmail) {
        Workspace workspace = workspaceService.getUserWorkspace(userEmail);
        UserStatsChart chart = new UserStatsChart();
        List<Collaborator> collaborators = collaboratorRepository.findAll();
        for (Collaborator collaborator : collaborators) {
            chart.addUser(collaborator.getFirstName() + " " +collaborator.getLastName().charAt(0),
                    (int) collaborator.getTasks()
                            .stream()
                            .filter(x -> x.getStatus() == Task.Status.DONE && x.getCompletedOn() != null && x.getCompletedOn().isAfter(LocalDate.now().minusMonths(1)))
                            .count());
        }
        log.info("Collaborator statistics chart generated");
        return chart;
    }
}