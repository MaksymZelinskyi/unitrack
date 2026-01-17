package com.unitrack.dto;

import lombok.Data;

@Data
public class StatisticsDto {

    private ProjectStatisticsDto projects;
    private TaskStatistics tasks;

}
