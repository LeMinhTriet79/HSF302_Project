package com.example.crm.controller;

import com.example.crm.entity.Task;
import com.example.crm.entity.User;
import com.example.crm.entity.Role;
import com.example.crm.security.CustomUserDetails;
import com.example.crm.service.TaskService;
import com.example.crm.service.TaskStatistics;
import com.example.crm.service.UserService;
import com.example.crm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserViewController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TaskService taskService;

    // Danh sách user
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user/list";
    }

    @GetMapping("/view/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        List<Task> tasks = taskService.getTasksByAssigneeId(id);

        model.addAttribute("user", user);
        model.addAttribute("tasks", tasks);
        return "admin/view"; // <--- BẮT BUỘC PHẢI NHƯ VẬY!
    }

    // Danh sách nhân viên cho leader
    @GetMapping("/employees")
    public String listEmployees(Model model) {
        model.addAttribute("users", userService.getAllEmployees());
        return "leader/employee-list";
    }

    // Form thêm user
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "user/form";
    }

    // Lưu user mới/sửa user
    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.createUser(user);
        return "redirect:/users";
    }

    // Form sửa user
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "user/form";
    }

    // Xóa user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.softDeleteUser(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa (ẩn) user thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa user: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/member/statistics")
    public String viewPersonalTaskStatistics(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        System.out.println("User role: " + userDetails.getAuthorities());
        TaskStatistics stats = taskService.getStatisticsForUser(userDetails.getId());
        model.addAttribute("stats", stats);
        return "member/taskStatistics";
    }

}
