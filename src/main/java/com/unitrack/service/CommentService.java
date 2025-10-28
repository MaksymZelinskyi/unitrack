package com.unitrack.service;

import com.unitrack.dto.request.CreateTaskDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Comment;
import com.unitrack.entity.Task;
import com.unitrack.exception.AuthenticationException;
import com.unitrack.exception.CommentNotFoundException;
import com.unitrack.exception.TaskNotFoundException;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.CommentRepository;
import com.unitrack.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CommentService {

    private TaskRepository taskRepository;
    private CommentRepository commentRepository;
    private CollaboratorRepository collaboratorRepository;

    public void addComment(Long taskId, CreateTaskDto dto, Principal principal) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("id", taskId));
        Comment replyTo = commentRepository.findById(dto.replyTo()).orElse(null);
        if (replyTo != null && !replyTo.getTask().equals(task)) throw new IllegalArgumentException("Comment replies to an unreachable comment");

        String email = principal.getName();
        Collaborator author = collaboratorRepository.findByEmail(email).orElseThrow(() -> new AuthenticationException("User not found"));
        Comment comment = new Comment();
        comment.setTask(task);
        comment.setText(dto.text());
        comment.setReplyTo(replyTo);
        comment.setAuthor(author);
        commentRepository.save(comment);
    }

    public void updateComment(Long id, Long taskId, CreateTaskDto dto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException("id", id));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("id", taskId));
        Comment replyTo = commentRepository.findById(dto.replyTo()).orElse(null);
        if (replyTo != null && !replyTo.getTask().equals(task)) throw new IllegalArgumentException("Comment replies to an unreachable comment");

        comment.setTask(task);
        comment.setText(dto.text());
        comment.setReplyTo(replyTo);
        commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
