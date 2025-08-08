package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.request.AssignmentDto;
import com.unitrack.entity.Role;
import com.unitrack.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/assignments")
@RequiredArgsConstructor
public class AssignmentController extends AuthenticatedController {

    private final AssignmentService assignmentService;
    private final AuthorizationService authService;

    @PostMapping("/")
    @PreAuthorize("@authService.canUpdate(#principal.getName(), #id)")
    public void addAssignment(AssignmentDto assignment, Principal principal) throws IllegalAccessException {
        assignmentService.add(assignment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authService.canUpdate(#principal.getName(), #id)")
    public void deleteAssignment(@PathVariable Long id, @RequestParam Long projectId, Principal principal) throws IllegalAccessException {
        assignmentService.remove(id);
    }
}