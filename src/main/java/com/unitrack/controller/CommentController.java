package com.unitrack.controller;

import com.unitrack.dto.request.CreateTaskDto;
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

    private CommentService commentService;

    @PostMapping("/")
    public String addComment(@RequestParam Long taskId, CreateTaskDto dto, Principal principal, HttpServletRequest request) {
        commentService.addComment(taskId, dto, principal);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PutMapping("/{id}")
    public String updateComment(@PathVariable Long id, @RequestParam Long taskId, CreateTaskDto dto, HttpServletRequest request) {
        commentService.updateComment(id, taskId, dto);

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