package com.unitrack.service;

import com.unitrack.dto.request.TaskDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Project;
import com.unitrack.entity.Task;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;
import com.unitrack.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final CollaboratorRepository collaboratorRepository;

    public void add(TaskDto dto, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Task task = new Task(dto.getTitle(), dto.getDescription(), dto.getDeadline(), project);
        List<Collaborator> assignees = dto.getAssignees()
                .stream()
                .map(x -> collaboratorRepository.findById(x.getId()).orElseThrow())
                .toList();
        task.addAssignees(assignees);
        task.setStatus(Task.Status.TODO);
        taskRepository.save(task);
    }

    public Task getByTitle(String title) {
        return taskRepository.findByTitle(title).orElse(null);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public Task update(Long id, TaskDto dto) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        taskRepository.save(task);
        return task;
    }

    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    public Set<Task> getByProject(Project project) {
        return taskRepository.findAllByProject(project);
    }

    public void setTaskStatus(Long id, String status) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setStatus(Task.Status.valueOf(status));
        taskRepository.save(task);
    }
}