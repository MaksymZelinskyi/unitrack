package com.unitrack.service;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.RecoveryCode;
import com.unitrack.exception.AuthenticationException;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.RecoveryCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

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
        byte[] bytes = new byte[8];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        String code = new String(bytes);
        RecoveryCode generatedCode = new RecoveryCode(code, collaborator);
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
}
