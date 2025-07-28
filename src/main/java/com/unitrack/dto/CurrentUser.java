package com.unitrack.dto;

import com.unitrack.entity.Collaborator;
import com.unitrack.repository.CollaboratorRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Data
@Component
public class CurrentUser {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;
    private String status;

}
