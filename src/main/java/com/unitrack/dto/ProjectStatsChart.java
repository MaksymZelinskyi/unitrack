package com.unitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ProjectStatsChart {

    private List<String> projects;
    private List<Integer> tasksPerProject;

    public ProjectStatsChart() {
        projects = new ArrayList<>();
        tasksPerProject = new ArrayList<>();
    }

    public void addProject(String projectName, int tasks) {
        projects.add(projectName);
        tasksPerProject.add(tasks);
    }
}
