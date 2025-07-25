package com.unitrack.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectTaskDto(Long id, String title, String description, List<String> assignees, LocalDateTime deadline, String status) {
}
