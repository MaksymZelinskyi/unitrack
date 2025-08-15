package com.unitrack.controller;

import com.unitrack.dto.request.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthenticationController {

    @GetMapping("/login")
    public String login(@Validated LoginDto dto) {
        return "login";
    }

}
