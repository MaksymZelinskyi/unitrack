package com.unitrack.controller;

import com.unitrack.service.InvitationService;
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
@RequestMapping("/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping("/new")
    @PreAuthorize("@authService.isAdmin(#principal.getName(), #workspaceId)")
    public String invite(@RequestParam("workspaceId") Long workspaceId, @RequestParam("collaboratorId") Long collaboratorId, Principal principal, HttpServletRequest request) {
        invitationService.inviteCollaborator(workspaceId, collaboratorId, principal.getName());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("@authService.canAcceptInvitation(#invitationId, #principal.getName())")
    public String acceptInvitation(@PathVariable("id") Long invitationId, Principal principal, HttpServletRequest request) {
        invitationService.acceptInvitation(invitationId);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
