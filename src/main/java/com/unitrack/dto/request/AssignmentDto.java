package com.unitrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AssignmentDto(@Positive Long collaboratorId, @Positive Long projectId, @NotBlank String role){
}
