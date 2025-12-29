package com.unitrack.dto;

import java.time.LocalDate;

public record ClientProjectDto(Long id, String title, String description, LocalDate start, LocalDate end, String status) {
}
