package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.request.CommentDto;
import com.unitrack.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/tasks/comments")
public class CommentController {

    private final CommentService commentService;
    private final AuthorizationService authService;

    @PostMapping("")
    public String addComment(@RequestParam Long taskId, CommentDto dto, Principal principal, HttpServletRequest request) {
        commentService.addComment(taskId, dto, principal);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authService.canUpdateComment(#principal.getName(), #id)")
    public String updateComment(@PathVariable Long id, CommentDto dto, HttpServletRequest request, Principal principal) {
        commentService.updateComment(id, dto);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authService.canDeleteComment(#principal.getName(), #id)")
    public String deleteComment(@PathVariable Long id, HttpServletRequest request, Principal principal) {
        commentService.deleteComment(id);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}