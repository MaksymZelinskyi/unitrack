package com.unitrack.service;

import com.unitrack.dto.request.CommentDto;
import com.unitrack.dto.request.UpdateCommentDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Comment;
import com.unitrack.entity.Task;
import com.unitrack.exception.AuthenticationException;
import com.unitrack.exception.CommentNotFoundException;
import com.unitrack.exception.TaskNotFoundException;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.CommentRepository;
import com.unitrack.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private static final Logger log = LoggerFactory.getLogger(CommentService.class);
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final CollaboratorRepository collaboratorRepository;

    public void addComment(Long taskId, CommentDto dto, Principal principal) {
        log.info("Saving a new comment. Task id: {}; text: {}", taskId, dto.getText());
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("id", taskId));
        Comment replyTo = null;
        if (dto.getReplyTo() != null)
            replyTo = commentRepository.findById(dto.getReplyTo()).orElse(null);

        if (replyTo != null && !replyTo.getTask().equals(task)) throw new IllegalArgumentException("Comment replies to an unreachable comment");

        String email = principal.getName();
        log.debug("Author email: {}", email);
        Collaborator author = collaboratorRepository.findByEmail(email).orElseThrow(() -> new AuthenticationException("User not found"));
        log.debug("Fetched author with id {}", author.getId());
        Comment comment = new Comment();
        comment.setTask(task);
        comment.setText(dto.getText());
        comment.setReplyTo(replyTo);
        comment.setAuthor(author);
        comment = commentRepository.save(comment);
        log.info("Saved comment with id {}", comment.getId());
    }

    public void updateComment(Long id, UpdateCommentDto dto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException("id", id));

        comment.setText(dto.getText());
        commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            comment.getReplies().forEach(x -> x.setReplyTo(null));
            commentRepository.deleteById(id);
        }
    }
}
