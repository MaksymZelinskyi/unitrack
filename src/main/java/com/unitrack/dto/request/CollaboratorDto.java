package com.unitrack.dto.request;

import java.util.List;

public record CollaboratorDto(String email, String firstName, String lastName, String password, List<ParticipationDto> projects) {

}
