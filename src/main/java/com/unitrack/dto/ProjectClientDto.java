package com.unitrack.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Valid
public record ProjectClientDto(Long id, @NotBlank(message = "Client name cannot be blank") String name){

}
