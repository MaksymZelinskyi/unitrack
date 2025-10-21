package com.unitrack.config;

import com.unitrack.entity.Collaborator;
import com.unitrack.exception.AuthenticationException;
import com.unitrack.exception.EntityNotFoundException;
import com.unitrack.exception.SecurityException;
import com.unitrack.repository.CollaboratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class InDbUserDetailsService implements UserDetailsService {

    private final CollaboratorRepository collaboratorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collaborator collaborator = collaboratorRepository
                .findByEmail(username)
                .orElseThrow(() -> new AuthenticationException("Collaborator with email " + username + " not found."));
        return new CustomUserDetails(collaborator.getId(), collaboratorRepository);
    }
}
