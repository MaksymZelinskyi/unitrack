package com.unitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class ProjectParticipationDto {

    private Long id;
    private String title;
    private String description;
    private Set<String> roles;
    private String status;
}
