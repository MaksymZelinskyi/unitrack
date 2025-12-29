package com.unitrack.unittest;

import com.unitrack.dto.request.CommentDto;
import com.unitrack.dto.request.UpdateCommentDto;
import com.unitrack.entity.Comment;
import com.unitrack.entity.Task;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.CommentRepository;
import com.unitrack.repository.TaskRepository;
import com.unitrack.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommentUnitTest {

    private CommentRepository commentRepository;
    private TaskRepository taskRepository;
    private CollaboratorRepository collaboratorRepository;
    private CommentService commentService;

    @BeforeEach
    public void init() {
        commentRepository = mock(CommentRepository.class);
        commentService = new CommentService(taskRepository, commentRepository, collaboratorRepository);
    }

    @Test
    public void testAllFieldsAreUpdated() {
        //arrange
        UpdateCommentDto dto = new UpdateCommentDto("Comment text", 1L);
        Comment comment = new Comment();
        Comment replyTo = new Comment();
        Task task = new Task("Task", "desc", null, null);
        replyTo.setId(1L);
        comment.setTask(task);
        replyTo.setTask(task);

        when(commentRepository.findById(2L)).thenReturn(Optional.of(comment));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(replyTo));

        //act
        commentService.updateComment(2L, dto);

        //assert
        assertEquals(dto.getText(), comment.getText());
        assertEquals(dto.getReplyTo(), comment.getReplyTo().getId());

        replyTo.setTask(new Task());

        assertThrows(IllegalArgumentException.class, () -> commentService.updateComment(2L, dto));
    }
}
