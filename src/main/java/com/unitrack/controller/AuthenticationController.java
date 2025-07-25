package com.unitrack.controller;

import com.unitrack.dto.request.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/login")
    public String login(LoginDto dto) {
        return "login";
    }
}
