package com.unitrack.unittest;

import com.unitrack.dto.request.AssigneeDto;
import com.unitrack.dto.request.UpdateProfileDto;
import com.unitrack.dto.request.UpdateProjectDto;
import com.unitrack.entity.*;
import com.unitrack.entity.Project;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;
import com.unitrack.repository.ProjectRepository;
import com.unitrack.service.ClientService;
import com.unitrack.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProjectUnitTest {
    
    private ProjectRepository projectRepository;
    private ProjectService projectService;
    private CollaboratorRepository collaboratorRepository;
    private ClientService clientService;

    @BeforeEach
    public void init() {
        projectRepository = mock(ProjectRepository.class);
        collaboratorRepository = mock(CollaboratorRepository.class);
        clientService = mock(ClientService.class);
        projectService = new ProjectService(projectRepository, collaboratorRepository, clientService, null);
    }

    @Test
    public void testAllFieldsAreUpdated() {
        UpdateProjectDto dto = new UpdateProjectDto("Project", "Lorem ipsum", "Client", LocalDate.now().minusMonths(1L), LocalDate.now());
        for (int i = 0; i < 5; i++) {
            AssigneeDto assigneeDto = new AssigneeDto((long) i,Role.TESTER.name(), "Assignee" + i);
            dto.getAssignees().add(assigneeDto);
            when(collaboratorRepository.findById((long)i)).thenReturn(Optional.of(new Collaborator()));
        }
        Project entity = new Project("title", "description", LocalDate.MIN, LocalDate.MAX);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(clientService.getByNameOrCreate("Client")).thenReturn(new Client("Client"));
        projectService.update(1L, dto);

        assertEquals(dto.getTitle(), entity.getTitle());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getClient(), entity.getClient().getName());
        assertEquals(dto.getStart(), entity.getStart());
        assertEquals(dto.getDeadline(), entity.getEnd());
        assertEquals(5, entity.getAssignees().size());
    }

}
