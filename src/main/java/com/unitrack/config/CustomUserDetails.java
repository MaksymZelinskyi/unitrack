package com.unitrack.config;

import com.unitrack.repository.CollaboratorRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final CollaboratorRepository collaboratorRepository;

    public CustomUserDetails(Long id, CollaboratorRepository collaboratorRepository) {
        this.id = id;
        this.collaboratorRepository = collaboratorRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authority = collaboratorRepository.findById(id).orElseThrow().isAdmin() ? "ROLE_ADMIN" : "ROLE_USER";
        return List.of(new SimpleGrantedAuthority(authority));
    }

    @Override
    public String getPassword() {
        return collaboratorRepository.findById(id).orElseThrow().getPassword();
    }

    @Override
    public String getUsername() {
        return collaboratorRepository.findById(id).orElseThrow().getEmail();
    }
}
