package com.example.crm.controller;

import com.example.crm.entity.User;
import com.example.crm.service.ProjectService;
import com.example.crm.service.RoleService;
import com.example.crm.service.TaskService;
import com.example.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        // Lấy tên & quyền user đang đăng nhập (admin)
        String username = principal.getName();
        User currentUser = userService.findByEmail(username);

        model.addAttribute("currentUserName", currentUser.getName());
        model.addAttribute("currentUserRole", currentUser.getRole().getName());

        // Thống kê tổng quan
        model.addAttribute("userCount", userService.count());
        model.addAttribute("roleCount", roleService.count());
        model.addAttribute("projectCount", projectService.count());
        model.addAttribute("taskCount", taskService.count());

        // Danh sách user (hiện bảng phía dưới)
        model.addAttribute("users", userService.getAllUsers());

        return "admin/dashboard";
    }



}
