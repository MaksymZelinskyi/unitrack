package com.unitrack.controller;

import com.unitrack.dto.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {


    @PostMapping("/login")
    public String login(LoginDto dto) {
        return "login";
    }
}
