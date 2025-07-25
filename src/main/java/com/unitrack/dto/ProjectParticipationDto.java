package com.unitrack.dto;

import java.util.Set;

public record ProjectParticipationDto(String title, String description, Set<String> roles) {
}
