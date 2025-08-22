package com.unitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class UserStatsChart {

    private List<String> users;
    private List<Integer> tasksPerUser;

    public UserStatsChart() {
        users = new ArrayList<>();
        tasksPerUser = new ArrayList<>();
    }

    public void addUser(String username, int tasks) {
        users.add(username);
        tasksPerUser.add(tasks);
    }
}