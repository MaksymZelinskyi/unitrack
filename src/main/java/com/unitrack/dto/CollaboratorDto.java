package com.unitrack.dto;

import java.util.List;

public record CollaboratorDto(Long id, String fullName, List<String> skills, List<String> projects) {
}
