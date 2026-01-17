package com.unitrack.dto;

import lombok.Data;

@Data
public class ProjectStatisticsDto {

    private int total;
    private int active;
    private int completed;
    private int deadlines;
}
