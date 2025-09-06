package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.request.AssignmentDto;
import com.unitrack.entity.Role;
import com.unitrack.service.AssignmentService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/assignments")
@RequiredArgsConstructor
public class AssignmentController extends AuthenticatedController {

    private final AssignmentService assignmentService;

    @PostMapping("/")
    @PreAuthorize("@authService.canUpdateOrDelete(#principal.getName(), #id)")
    public void addAssignment(@Validated AssignmentDto assignment, Principal principal) {
        assignmentService.add(assignment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authService.canUpdateOrDelete(#principal.getName(), #id)")
    public void deleteAssignment(@Positive @PathVariable Long id,
                                 @Positive @RequestParam Long projectId,
                                 Principal principal) {
        assignmentService.remove(id);
    }
}