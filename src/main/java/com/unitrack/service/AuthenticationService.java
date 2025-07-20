package com.unitrack.service;

import com.unitrack.dto.LoginDto;
import com.unitrack.repository.CollaboratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final CollaboratorRepository collaboratorRepository;

    public void login(LoginDto dto) {

    }
}
