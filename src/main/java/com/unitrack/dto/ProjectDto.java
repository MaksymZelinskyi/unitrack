package com.unitrack.dto;

import java.time.LocalDate;

public record ProjectDto(Long id, String title, String description, ProjectClientDto client,
                         LocalDate start, LocalDate end, String status) {

}
