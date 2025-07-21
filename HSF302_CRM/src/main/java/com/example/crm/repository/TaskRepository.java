package com.example.crm.repository;

import com.example.crm.entity.Project;
import com.example.crm.entity.Task;
import com.example.crm.entity.User;
import com.example.crm.service.TaskStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repository quản lý bảng Task (công việc)
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Lấy tất cả task của một project
    List<Task> findByProject(Project project);

    // Lấy tất cả task được giao cho một user
    List<Task> findByAssignee(User assignee);

    List<Task> findByAssigneeEmail(String email);

    List<Task> findByProjectIdAndAssigneeId(Long projectId, Long assigneeId);

    List<Task> findByAssigneeId(Long assigneeId);

    @Query(value = """
        SELECT 
            COUNT(*) AS totalTasks,
            SUM(CASE WHEN status_id = 3 THEN 1 ELSE 0 END) AS completedTasks,
            SUM(CASE WHEN status_id != 3 AND end_date < GETDATE() THEN 1 ELSE 0 END) AS overdueTasks
        FROM tasks
        WHERE assignee_id = :userId
        """, nativeQuery = true)
    TaskStatistics getTaskStatisticsByUserId(@Param("userId") Long userId);

}
