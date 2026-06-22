package com.unitrack.controller;

import com.unitrack.dto.WorkspaceDto;
import com.unitrack.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class RestWorkspaceController {

    private final WorkspaceService workspaceService;

    @GetMapping("/search")
    public Page<WorkspaceDto> searchWorkspace(String query,
                                              @RequestParam(name = "pagenumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(name = "pagesize", defaultValue = "5") int pageSize)  {
        return workspaceService.searchWorkspaces(query, Pageable.ofSize(pageSize).withPage(pageNumber));
    }

}
