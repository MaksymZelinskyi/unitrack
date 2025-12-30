package com.unitrack.controller;

import com.unitrack.dto.request.LoginDto;
import com.unitrack.dto.request.RegisterDto;
import com.unitrack.dto.request.ResetPasswordDto;
import com.unitrack.service.AccountService;
import com.unitrack.service.CollaboratorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/")
@Slf4j
public class AuthenticationController {

    private final AccountService accountService;
    private final CollaboratorService collaboratorService;

    @GetMapping("/login")
    public String login(@Validated LoginDto dto) {
        return "login";
    }

    @GetMapping("/password/reset")
    public String resetPassword(@RequestParam(required = false) boolean codeSent) {
        return "reset-password";
    }

    @GetMapping("/password/send-code")
    public String sendRecoveryCode(@RequestParam String email) {
        try {
            accountService.sendRecoveryCode(email);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return "redirect:/password/reset?codeSent=true";
    }

    @PostMapping("/password/reset")
    public String resetPassword(@RequestBody ResetPasswordDto dto, HttpServletRequest request) {
        try {
            if (!accountService.verifyRecoveryCode(dto.getEmail(), dto.getResetCode())) {
                return "redirect:/password/reset?error=true";
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            return "redirect:/password/reset?error=true";
        }

        accountService.changePassword(dto.getEmail(), dto.getPassword());
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Validated RegisterDto dto) {
        collaboratorService.register(dto);
        return "/login";
    }

}