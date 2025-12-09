package com.unitrack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Data
public class RecoveryCode {

    @Id
    @GeneratedValue
    private Long id;

    private String code;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne
    private Collaborator collaborator;

    public RecoveryCode(String code, Collaborator collaborator) {
        this.code = code;
        this.collaborator = collaborator;
    }

}
