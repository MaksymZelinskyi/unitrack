package com.unitrack.dto.request;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public record LoginDto(@Email String email, @Length(min = 8, max = 255) String password) {

}
