package com.unitrack.unittest;

import com.unitrack.dto.ProjectStatsChart;
import com.unitrack.dto.UserStatsChart;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Project;
import com.unitrack.entity.Task;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;
import com.unitrack.repository.TaskRepository;
import com.unitrack.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatisticsUnitTest {

    private ProjectRepository projectRepository;
    private CollaboratorRepository collaboratorRepository;
    private TaskRepository taskRepository;
    private StatisticsService statisticsService;

    @BeforeEach
    public void init() {
        projectRepository = mock(ProjectRepository.class);
        collaboratorRepository = mock(CollaboratorRepository.class);
        statisticsService = new StatisticsService(projectRepository, collaboratorRepository, taskRepository);
    }

    @Test
    public void testProjectStatsGeneratedProperly() {
        //arrange
        List<Project> projects = new ArrayList<>();
        Project project = new Project();
        project.setTitle("Project");
        Task task1 = new Task();
        task1.setStatus(Task.Status.DONE);
        task1.setCompletedOn(LocalDate.now().minusDays(1L));

        Task task2 = new Task();
        task2.setStatus(Task.Status.TODO);

        Task task3 = new Task();
        task3.setStatus(Task.Status.IN_PROGRESS);
        task3.setCompletedOn(LocalDate.now().minusDays(1L));

        project.getTasks().addAll(List.of(task1, task2, task3));
        projects.add(project);

        when(projectRepository.findAll()).thenReturn(projects);

        //act
        ProjectStatsChart projectStatsChart = statisticsService.getProjectChart();

        //assert
        assertEquals("Project", projectStatsChart.getProjects().get(0));
        assertEquals(1, projectStatsChart.getTasksPerProject().get(0));
    }

    @Test
    public void testCollabStatsGeneratedProperly() {
        //arrange
        List<Collaborator> collaborators = new ArrayList<>();
        Collaborator collaborator = new Collaborator();
        collaborator.setFirstName("John");
        collaborator.setLastName("Doe");
        Task task1 = new Task();
        task1.setStatus(Task.Status.DONE);
        task1.setCompletedOn(LocalDate.now().minusDays(1L));

        Task task2 = new Task();
        task2.setStatus(Task.Status.TODO);

        Task task3 = new Task();
        task3.setStatus(Task.Status.IN_PROGRESS);
        task3.setCompletedOn(LocalDate.now().minusDays(1L));

        collaborator.getTasks().addAll(List.of(task1, task2, task3));
        collaborators.add(collaborator);

        when(collaboratorRepository.findAll()).thenReturn(collaborators);

        //act
        UserStatsChart projectStatsChart = statisticsService.getUserChart();

        //assert
        assertEquals("John D", projectStatsChart.getUsers().get(0));
        assertEquals(1, projectStatsChart.getTasksPerUser().get(0));
    }
}
