package com.unitrack.dto;

import java.util.List;

public record CollaboratorDto (
     Long id,
     String name,
     String avatarUrl,
     List<String> skills,
     List<String> projects
) {

}