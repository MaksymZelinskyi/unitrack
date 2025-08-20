package com.unitrack.dto;

import lombok.Data;

@Data
public class TaskStatistics {

    private int total;
    private int inTime;
    private int completed;
    private int deadlines;
    private int overdue;
}

