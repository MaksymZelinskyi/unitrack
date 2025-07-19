package com.unitrack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Skill {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Skill(String name) {
        this.name = name;
    }

}
