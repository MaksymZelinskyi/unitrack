package com.unitrack.dto;

import java.time.LocalDateTime;

public record TaskDto(String title, String description, String project, LocalDateTime deadline) {
}
