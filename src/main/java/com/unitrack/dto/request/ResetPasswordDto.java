package com.unitrack.dto.request;

public record ResetPasswordDto (String email, String resetCode, String password) {

}
