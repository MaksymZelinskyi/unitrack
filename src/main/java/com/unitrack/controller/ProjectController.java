package com.unitrack.controller;

import com.unitrack.dto.CollaboratorInListDto;
import com.unitrack.dto.ProjectTaskDto;
import com.unitrack.dto.request.ProjectDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Project;
import com.unitrack.service.CollaboratorService;
import com.unitrack.service.ProjectService;
import com.unitrack.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
@SessionAttributes({"collaborators", "assignees"})
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;
    private final CollaboratorService collaboratorService;

    @ModelAttribute("collaborators")
    public Set<CollaboratorInListDto> collaborators() {
        return collaboratorService.getAll().stream().map(x->new CollaboratorInListDto(x.getId(), x.getFirstName()+" "+x.getLastName(), x.getAvatarUrl())).collect(Collectors.toSet());
    }

    @ModelAttribute("assignees")
    public Set<CollaboratorInListDto> assignees() {
        return new HashSet<>();
    }


    @GetMapping("/{id}")
    public String getProjectById(@PathVariable Long id, Model model) {
        Project project = projectService.getById(id);
        List<ProjectTaskDto> tasks = taskService.getByProject(project)
                .stream()
                .map(x->new ProjectTaskDto(x.getId(), x.getTitle(), x.getDescription(),
                        x.getAssignees().stream().map(y->y.getFirstName() + " " + y.getLastName()).toList(),
                        x.getDeadline(), x.getStatus().name()))
                .toList();
        List<ProjectTaskDto> todo = new ArrayList<>();
        List<ProjectTaskDto> inProgress = new ArrayList<>();
        List<ProjectTaskDto> done = new ArrayList<>();
        for(ProjectTaskDto task : tasks) {
            switch(task.status) {
                case "TODO":
                    todo.add(task);
                    break;
                case "IN_PROGRESS":
                    inProgress.add(task);
                    break;
                case "DONE":
                    done.add(task);
            }
        }
        model.addAttribute("project", new com.unitrack.dto.ProjectDto(project.getId(), project.getTitle(), project.getDescription(), project.getClient().getName(), project.getStart(), project.getEnd()));
        model.addAttribute("todo", todo);
        model.addAttribute("in_progress", inProgress);
        model.addAttribute("done", done);
        return "project-page";
    }

    @GetMapping("/new")
    public String newProject(Model model) {
        return "new-project";
    }

    @PostMapping("/new")
    public String newProject(@RequestBody ProjectDto dto) {
        projectService.add(dto);
        return "redirect:/projects";
    }

    @PutMapping("/{id}")
    public Project updateProject(@PathVariable Long id, ProjectDto project) {
        return projectService.update(id, project);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.delete(id);
    }

    @GetMapping("/{id}/members")
    public List<Collaborator> getMembers(@PathVariable Long id) {
        return projectService.getMembers(id);
    }

}
