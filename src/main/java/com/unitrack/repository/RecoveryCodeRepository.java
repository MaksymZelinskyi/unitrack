package com.unitrack.repository;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.RecoveryCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecoveryCodeRepository extends JpaRepository<RecoveryCode, Long> {

    Optional<RecoveryCode> findByCode(String code);

    Optional<RecoveryCode> findByCollaborator(Collaborator collaborator);
}
