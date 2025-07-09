package com.example.crm.service;

import com.example.crm.entity.Project;
import com.example.crm.entity.ProjectMember;
import com.example.crm.entity.User;
import com.example.crm.repository.ProjectMemberRepository;
import com.example.crm.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMemberService {
    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Autowired
    private ProjectRepository projectRepository;

    // Lấy tất cả thành viên dự án
    public List<ProjectMember> getAllProjectMembers() {
        return projectMemberRepository.findAll();
    }

    // Lấy tất cả thành viên dự án thuộc các dự án do leader quản lý
    public List<ProjectMember> getProjectMembersByLeader(Long leaderId) {
        User leader = new User();
        leader.setId(leaderId);
        List<Project> projects = projectRepository.findByLeader(leader);
        List<ProjectMember> result = new java.util.ArrayList<>();
        for (Project project : projects) {
            result.addAll(projectMemberRepository.findByProject(project));
        }
        return result;
    }

    // Tạo mới thành viên dự án
    public ProjectMember createProjectMember(ProjectMember pm) {
        return projectMemberRepository.save(pm);
    }

    // Xóa thành viên dự án
    public void deleteProjectMember(Long id) {
        projectMemberRepository.deleteById(id);
    }

    // Lấy thành viên dự án theo id
    public ProjectMember getProjectMemberById(Long id) {
        return projectMemberRepository.findById(id).orElseThrow();
    }
}
