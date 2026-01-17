package com.unitrack.dto;

import java.time.LocalDateTime;

public record TaskInListDto(Long id, String title, String description, LocalDateTime deadline, boolean completed) {

}
