package com.unitrack.config;

import com.unitrack.entity.Collaborator;
import com.unitrack.repository.CollaboratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
        Collaborator collaborator = collaboratorRepository.findByEmail(username).orElseThrow();
        String role = collaborator.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER";
        return new User(collaborator.getEmail(), collaborator.getPassword(), Collections.singleton(new SimpleGrantedAuthority(role)));
    }
}
