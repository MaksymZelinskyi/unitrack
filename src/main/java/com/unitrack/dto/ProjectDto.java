package com.unitrack.dto;

import java.time.LocalDateTime;

public record ProjectDto(String title, String description, String client, LocalDateTime start, LocalDateTime end) {
}
