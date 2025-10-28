package com.unitrack.controller;

import com.unitrack.dto.request.CreateTaskDto;
import com.unitrack.service.CommentService;
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
    public void addComment(@RequestParam Long taskId, CreateTaskDto dto, Principal principal) {
        commentService.addComment(taskId, dto, principal);
    }

    @PutMapping("/{id}")
    public void updateComment(@PathVariable Long id, @RequestParam Long taskId, CreateTaskDto dto) {
        commentService.updateComment(id, taskId, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }
}
