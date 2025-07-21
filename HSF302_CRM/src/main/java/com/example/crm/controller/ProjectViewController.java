package com.example.crm.controller;

import com.example.crm.entity.Project;
import com.example.crm.entity.User;
import com.example.crm.security.CustomUserDetails;
import com.example.crm.service.ProjectService;
import com.example.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.crm.security.CustomUserDetails;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectViewController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    // Danh sách dự án
    @GetMapping
    public String listProjects(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails.getRole().equals("LEADER")) {
            model.addAttribute("projects", projectService.getProjectsByLeader(userDetails.getId()));
        } else {
            model.addAttribute("projects", projectService.getAllProjects());
        }
        if (userDetails.getRole().equals("ADMIN")) {
            return "admin/project";
        }
        return "project/list";
    }

    // Form thêm dự án
    @GetMapping("/create")
    public String showCreateForm(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("project", new Project());
        if (userDetails.getRole().equals("LEADER")) {
            model.addAttribute("leaders", java.util.Collections.singletonList(userService.getUserById(userDetails.getId())));
        } else {
            model.addAttribute("leaders", userService.getAllUsers());
        }
        if (userDetails.getRole().equals("ADMIN")) {
            return "admin/project-form";
        }
        return "project/form";
    }

    // Lưu dự án mới/sửa dự án
    @PostMapping("/save")
    public String saveProject(@ModelAttribute("project") Project project, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (project.getId() == null) {
            // Tạo mới
            if (userDetails.getRole().equals("LEADER")) {
                projectService.createProject(project, userDetails.getId());
            } else {
                projectService.createProject(project, project.getLeader().getId());
            }
        } else {
            // Cập nhật
            projectService.updateProject(project.getId(), project);
        }

        return "redirect:/projects";
    }
//    @PostMapping("/save")
//    public String saveProject(@ModelAttribute("project") Project project, @AuthenticationPrincipal CustomUserDetails userDetails) {
//        if (userDetails.getRole().equals("LEADER")) {
//            // Bắt buộc leader là chính mình
//            projectService.createProject(project, userDetails.getId());
//        } else {
//            projectService.createProject(project, project.getLeader().getId());
//        }
//        return "redirect:/projects";
//    }

    // Form sửa dự án
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Project project = projectService.getProjectById(id);
        if (userDetails.getRole().equals("LEADER") && !project.getLeader().getId().equals(userDetails.getId())) {
            return "redirect:/projects";
        }
        model.addAttribute("project", project);
        if (userDetails.getRole().equals("LEADER")) {
            model.addAttribute("leaders", java.util.Collections.singletonList(userService.getUserById(userDetails.getId())));
        } else {
            model.addAttribute("leaders", userService.getAllUsers());
        }
        if (userDetails.getRole().equals("ADMIN")) {
            return "admin/project-form";
        }
        return "project/form";
    }

    // Xóa dự án
    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Project project = projectService.getProjectById(id);
        if (userDetails.getRole().equals("LEADER") && !project.getLeader().getId().equals(userDetails.getId())) {
            return "redirect:/projects";
        }
        projectService.deleteProject(id);
        return "redirect:/projects";
    }


    //dt
    @GetMapping("/member/viewProjects")
    public String viewProjectByMemberID(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<Project> projects = projectService.getProjectsByUserId(userDetails.getId());
        model.addAttribute("projects", projects);
        return "member/memberProject";
    }
}
