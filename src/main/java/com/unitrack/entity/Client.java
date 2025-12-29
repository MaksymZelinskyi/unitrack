package com.unitrack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An entity representing the customers
 * Not to confound with collaborators - the employees(equally users)
 */
@Data
@Entity
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;
    private String phoneNumber;

    @ManyToOne
    private Workspace workspace;

    public Client(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Client(String name) {
        this.name = name;
    }
}

