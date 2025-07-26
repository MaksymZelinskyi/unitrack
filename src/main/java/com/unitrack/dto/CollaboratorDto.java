package com.unitrack.dto;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CollaboratorDto {
    public Long id;
    public String name;
    public String avatarUrl;
    public List<String> skills;
    public List<String> projects;
}
