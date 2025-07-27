package com.unitrack.dto.request;

import java.time.LocalDate;
import java.util.Set;

public record ProjectDto(String title, String description, String client, LocalDate start, LocalDate deadline, Set<AssigneeDto> assignees) {
}
