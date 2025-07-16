package com.unitrack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Collaborator {

    @Id
    @GeneratedValue
    private Long id;

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private GlobalRole globalRole;

    public Collaborator(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    enum GlobalRole {
        ADMIN, USER
    }
}
