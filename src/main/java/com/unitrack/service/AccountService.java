package com.unitrack.service;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.RecoveryCode;
import com.unitrack.exception.AuthenticationException;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.RecoveryCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.SecureRandomParameters;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final CollaboratorRepository collaboratorRepository;
    private final MailService mailService;
    private final RecoveryCodeRepository recoveryCodeRepository;

    public void changePassword(String email, String newPassword) {
        Collaborator collaborator = collaboratorRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User with email " + email + " not found"));
        collaborator.setPassword(newPassword);
        collaboratorRepository.save(collaborator);
    }

    public void sendRecoveryCode(String email) {
        Collaborator collaborator = collaboratorRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User with email " + email + " not found"));
        Optional<RecoveryCode> optional = recoveryCodeRepository.findByCollaborator(collaborator);
        if (optional.isPresent()) {
            recoveryCodeRepository.delete(optional.get());
        }

        String code = generateRandomString();
        mailService.send(email, "UniTrack password recovery", "Your password-recovery code is: " + code);
    }

    public boolean verifyRecoveryCode(String email, String code) {
        Collaborator collaborator = collaboratorRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User with email " + email + " not found"));
        Optional<RecoveryCode> optional = recoveryCodeRepository.findByCollaborator(collaborator);
        if (!optional.isPresent()) {
            return false;
        }
        RecoveryCode recoveryCode = optional.get();
        if (recoveryCode.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(30))) {
            recoveryCodeRepository.delete(recoveryCode);
            return false;
        }

        return recoveryCode.getCode().equals(code);
    }

    private String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append((char)(random.nextInt('0', 'z')));
        }
        return sb.toString();
    }
}
