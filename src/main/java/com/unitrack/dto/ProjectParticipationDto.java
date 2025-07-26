package com.unitrack.dto;

import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class ProjectParticipationDto {

    public Long id;
    public String title;
    public String description;
    public Set<String> roles;

}
