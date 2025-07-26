package com.unitrack.dto;

import java.util.Set;

public record ProjectParticipationDto(Long id, String title, String description, Set<String> roles) {
}
