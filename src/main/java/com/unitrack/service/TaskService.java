package com.unitrack.service;

import com.unitrack.dto.TaskDto;
import com.unitrack.entity.Task;
import com.unitrack.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Task updateById(Long id, TaskDto dto) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        taskRepository.save(task);
        return task;
    }
}
