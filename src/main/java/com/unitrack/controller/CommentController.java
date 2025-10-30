package com.unitrack.controller;

import com.unitrack.dto.request.CommentDto;
import com.unitrack.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/tasks/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    public String addComment(@RequestParam Long taskId, CommentDto dto, Principal principal, HttpServletRequest request) {
        commentService.addComment(taskId, dto, principal);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PutMapping("/{id}")
    public String updateComment(@PathVariable Long id, CommentDto dto, HttpServletRequest request) {
        commentService.updateComment(id, dto);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable Long id, HttpServletRequest request) {
        commentService.deleteComment(id);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}