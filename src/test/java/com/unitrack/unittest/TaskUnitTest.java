package com.unitrack.unittest;

import com.unitrack.dto.CollaboratorInListDto;
import com.unitrack.dto.request.UpdateTaskDto;
import com.unitrack.entity.*;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;
import com.unitrack.repository.TaskRepository;
import com.unitrack.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class TaskUnitTest {

    private TaskRepository taskRepository;
    private TaskService taskService;
    private ProjectRepository projectRepository;
    private CollaboratorRepository collaboratorRepository;

    @BeforeEach
    public void init() {
        taskRepository = mock(TaskRepository.class);
        projectRepository = mock(ProjectRepository.class);
        collaboratorRepository = mock(CollaboratorRepository.class);
        taskService = new TaskService(taskRepository, projectRepository, collaboratorRepository);
    }

    @Test
    public void testAllFieldsAreUpdated() {
        UpdateTaskDto dto = new UpdateTaskDto("Task", "Lorem ipsum", LocalDateTime.now());
        Project project = new Project();

        for (int i = 1; i <= 5; i++) {
            Collaborator collaborator = new Collaborator();
            project.addAssignee(new Participation(collaborator, project, Role.TESTER));
            dto.getAssignees().add(new CollaboratorInListDto((long) i, "Collaborator" + i, "url"));
            when(collaboratorRepository.findById((long) i)).thenReturn(Optional.of(collaborator));
        }
        CollaboratorInListDto wrongCollab = new CollaboratorInListDto(6L, "Anonymous", "url"); // not engaged in the project
        dto.getAssignees().add(wrongCollab);
        when(collaboratorRepository.findById(6L)).thenReturn(Optional.of(new Collaborator()));

        Task entity = new Task();
        project.getTasks().add(entity);
        entity.setProject(project);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(entity));

        taskService.update(1L, dto);

        assertEquals(dto.getTitle(), entity.getTitle());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getDeadline(), entity.getDeadline());
        assertEquals(5, entity.getAssignees().size());
    }
}
