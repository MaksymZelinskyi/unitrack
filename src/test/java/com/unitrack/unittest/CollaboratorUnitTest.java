package com.unitrack.unittest;

import com.unitrack.dto.request.UpdateProfileDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Participation;
import com.unitrack.entity.Project;
import com.unitrack.entity.Role;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.service.CollaboratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CollaboratorUnitTest {

    private CollaboratorRepository collaboratorRepository;
    private CollaboratorService collaboratorService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void init() {
        collaboratorRepository = mock(CollaboratorRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        collaboratorService = new CollaboratorService(collaboratorRepository, null, null, passwordEncoder, null, null);
    }

    @Test
    public void testAllFieldsAreUpdated() {
        //arrange
        UpdateProfileDto dto = new UpdateProfileDto("John", "Doe", "url", "john@doe.com", "password");
        Collaborator entity = new Collaborator("name", "name", "email", "pass");
        when(collaboratorRepository.findByEmail("email")).thenReturn(Optional.of(entity));
        //act
        collaboratorService.update("email", dto);
        //assert
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getAvatarUrl(), entity.getAvatarUrl());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertTrue(passwordEncoder.matches(dto.getPassword(), entity.getPassword()));
    }

    @Test
    public void testDeleteClearsParticipationList() {
        //arrange
        Collaborator collaborator = new Collaborator();
        for (int i = 0; i < 5; i++) {
            Project project = new Project();
            Participation participation = new Participation(collaborator, project, Role.TESTER);
            project.addAssignee(participation);
            collaborator.addProject(participation);
        }
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaborator));

        assertEquals(5, collaborator.getProjects().size(), "Projects not added");

        //act
        collaboratorService.delete(1L);

        //assert
        assertEquals(0, collaborator.getProjects().size());
    }
}