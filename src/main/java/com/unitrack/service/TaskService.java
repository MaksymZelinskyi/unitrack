package com.unitrack.service;

import com.unitrack.dto.CollaboratorInListDto;
import com.unitrack.dto.request.TaskDto;
import com.unitrack.dto.request.UpdateTaskDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Project;
import com.unitrack.entity.Task;
import com.unitrack.exception.CollaboratorNotFoundException;
import com.unitrack.exception.ProjectNotFoundException;
import com.unitrack.exception.TaskNotFoundException;
import com.unitrack.repository.CollaboratorRepository;
import com.unitrack.repository.ProjectRepository;
import com.unitrack.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final CollaboratorRepository collaboratorRepository;

    public void add(TaskDto dto, Long projectId) {
        //retrieve project
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("id", projectId));

        //create task entity
        Task task = new Task(dto.getTitle(), dto.getDescription(), dto.getDeadline(), project);
        List<Collaborator> assignees = dto.getAssignees()
                .stream()
                .map(x -> collaboratorRepository.findById(x.getId()).orElseThrow(() -> new CollaboratorNotFoundException("id", x.getId())))
                .toList();
        task.addAssignees(assignees);
        //set default status
        task.setStatus(Task.Status.TODO);

        //persist
        task = taskRepository.save(task);
        log.info("Task with id {} saved: {}", task.getId(), task.getTitle());
    }

    public Task getByTitle(String title) {
        Task task = taskRepository.findByTitle(title).orElse(null);
        if(task == null) log.debug("Task with title {} not found", title);
        else log.debug("Task with title {} extracted. Id: {}", title, task.getId());
        return task;
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
        log.info("Task with id {} deleted", id);
    }

    public Task update(Long id, UpdateTaskDto dto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("id", id));
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDeadline(dto.getDeadline());
        task.getAssignees().clear();
        //todo: take assignees from the project only
        for (CollaboratorInListDto c : dto.getAssignees()) {
            task.getAssignees().add(collaboratorRepository
                    .findById(c.getId())
                    .orElseThrow(() -> new CollaboratorNotFoundException("id", c.getId()))
            );
        }
        taskRepository.save(task);
        log.info("Task with id {} updated: {}", id, task.getTitle());
        return task;
    }

    public Task getById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("id", id));
        log.debug("Task with id {} extracted: {}", id, task.getTitle());
        return task;
    }

    public Set<Task> getByProject(Project project) {
        Set<Task> tasks = taskRepository.findAllByProject(project);
        log.debug("Tasks of project with id {} extracted. {} tasks found", project.getId(), tasks.size());
        return tasks;
    }

    public void setTaskStatus(Long id, String status) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("id", id));
        task.setStatus(Task.Status.valueOf(status));
        taskRepository.save(task);
        log.info("The status of task {}: \"{}\" changed to {}", id, task.getTitle(), task.getStatus());
    }

    public void markTaskCompleted(Long id, boolean completed) {
        if (completed) {
            setTaskStatus(id, "DONE");
        } else {
            setTaskStatus(id, "TODO");
        }
    }
}