package com.unitrack.dto;

import java.time.LocalDate;

public record ProjectParticipationDto(Long id, String title, String description, String role, String status, LocalDate deadline) {

}