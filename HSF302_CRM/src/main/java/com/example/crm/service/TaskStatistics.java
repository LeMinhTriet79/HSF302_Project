package com.example.crm.service;

public interface TaskStatistics {
    Long getTotalTasks();
    Long getCompletedTasks();
    Long getOverdueTasks();
}
