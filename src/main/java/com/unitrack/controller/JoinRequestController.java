package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.service.JoinRequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/join-requests")
@RequiredArgsConstructor
public class JoinRequestController {

    private final JoinRequestService joinRequestService;
    private final AuthorizationService authService;

    @PostMapping("/new")
    public String join(@RequestParam("workspaceId") Long workspaceId, Principal principal, HttpServletRequest request) {
        joinRequestService.sendJoinRequest(workspaceId, principal.getName());

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("@authService.canAcceptJoinRequest(#requestId, #principal.getName())")
    public String acceptRequest(@PathVariable("id") Long requestId, Principal principal, HttpServletRequest request) {
        joinRequestService.acceptRequest(requestId);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
