package com.unitrack.service;

import com.unitrack.dto.request.TaskDto;
import com.unitrack.entity.Project;
import com.unitrack.entity.Task;
import com.unitrack.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public void add(TaskDto dto) {
        Task task = new Task(dto.title(), dto.description());
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
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        taskRepository.save(task);
        return task;
    }

    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    public Set<Task> getByProject(Project project) {
        return taskRepository.findAllByProject(project);
    }
}
