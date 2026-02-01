package com.unitrack.dto;

import java.util.List;

public record CollaboratorProjectDto(Long projectId, String title, String role, List<TaskInListDto> tasks) {
}
