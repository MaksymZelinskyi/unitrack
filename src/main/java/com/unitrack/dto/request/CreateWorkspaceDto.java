package com.unitrack.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateWorkspaceDto(
        @NotBlank String name, @NotBlank String description) {
}
