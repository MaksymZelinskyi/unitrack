package com.unitrack;

import com.unitrack.dto.ProjectStatsChart;
import com.unitrack.dto.StatisticsDto;
import com.unitrack.dto.UserStatsChart;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Project;
import com.unitrack.entity.Task;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;
import com.unitrack.repository.TaskRepository;
import com.unitrack.service.StatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatsTests {

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @InjectMocks
    private StatisticsService statisticsService;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TaskRepository taskRepository;

    @Test
    public void testGeneralStats() {

        when(projectRepository.count()).thenReturn(1L);
        when(taskRepository.count()).thenReturn(1L);
        when(projectRepository.countByEndBetween(LocalDate.now().minusMonths(1L), LocalDate.now())).thenReturn(1);
        when(taskRepository.countByCompletedOnAfter(LocalDate.now().minusMonths(1L))).thenReturn(1);

        StatisticsDto dto = statisticsService.getStats("");
        assertNotNull(dto);
        assertEquals(1, dto.getProjects().getTotal());
        assertEquals(1, dto.getTasks().getTotal());
        assertEquals(1, dto.getProjects().getDeadlines());
        assertEquals(1, dto.getTasks().getDeadlines());
    }


    @Test
    public void testCollaboratorStats() {
        Collaborator collaborator = new Collaborator("Collaborator", "Mock", "collaborator@gmail.com", "password");
        Task task = new Task("Task1", "description", LocalDateTime.now(), null);
        task.setCompletedOn(LocalDate.now().minusWeeks(1));
        collaborator.setTasks(Set.of(task));
        when(collaboratorRepository.findAll()).thenReturn(List.of(collaborator));

        UserStatsChart usc = statisticsService.getUserChart();
        assertNotNull(usc);
        assertEquals("Collaborator M", usc.getUsers().get(0));
        assertEquals(1, usc.getTasksPerUser().get(0));
    }

    @Test
    public void testProjectStats() {
        Project project = new Project("project", "Mock", LocalDate.now().minusMonths(1), LocalDate.now());
        Task task = new Task("Task1", "description", LocalDateTime.now(), project);
        task.setCompletedOn(LocalDate.now().minusWeeks(1));
        project.setTasks(Set.of(task));
        when(projectRepository.findAll()).thenReturn(List.of(project));

        ProjectStatsChart psc = statisticsService.getProjectChart();
        assertNotNull(psc);
        assertEquals(project.getTitle(), psc.getProjects().get(0));
        assertEquals(1, psc.getTasksPerProject().get(0));
    }
}
