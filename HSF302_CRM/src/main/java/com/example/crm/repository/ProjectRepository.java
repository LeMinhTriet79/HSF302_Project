package com.example.crm.repository;

import com.example.crm.entity.Project;
import com.example.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Repository quản lý bảng Project (dự án)
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Lấy danh sách dự án do một leader quản lý
    List<Project> findByLeader(User leader);


    @Query("SELECT p FROM Project p " +
            "JOIN p.members pm " +
            "WHERE pm.user.id = :userId")
    List<Project> findProjectsByMemberUserId(@Param("userId") Long userId);

}
