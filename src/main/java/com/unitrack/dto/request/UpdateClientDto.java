package com.unitrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record UpdateClientDto(@NotBlank(message = "Client name must not be null") String name, String email, String phoneNumber) {
}
