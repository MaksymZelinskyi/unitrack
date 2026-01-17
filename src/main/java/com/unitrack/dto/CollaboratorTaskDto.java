package com.unitrack.dto;

import java.time.LocalDateTime;

public record CollaboratorTaskDto(Long id, String title, String description, String project, LocalDateTime deadline) {
}
