package com.unitrack.dto;


import java.time.LocalDateTime;

public record ProjectDto(String title, String description, LocalDateTime start, LocalDateTime end, String status) {

}
