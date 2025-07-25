package com.unitrack.controller;

import com.unitrack.config.AuthorizationService;
import com.unitrack.dto.request.AssignmentDto;
import com.unitrack.entity.Role;
import com.unitrack.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AuthorizationService authService;

    @PostMapping("/")
    public void addAssignment(AssignmentDto assignment, Principal principal) throws IllegalAccessException {
        if(!authService.hasRole(principal.getName(), assignment.projectId(), Role.MANAGER))
            throw new IllegalAccessException();

        assignmentService.add(assignment);
    }

    @DeleteMapping("/{id}")
    public void deleteAssignment(@PathVariable Long id, @RequestParam Long projectId, Principal principal) throws IllegalAccessException {
        if(!authService.hasRole(principal.getName(), projectId, Role.MANAGER))
            throw new IllegalAccessException();
        assignmentService.remove(id);
    }
}