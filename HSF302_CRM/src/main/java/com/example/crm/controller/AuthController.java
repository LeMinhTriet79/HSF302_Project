package com.example.crm.controller;

import com.example.crm.entity.User;
import com.example.crm.repository.UserRepository;
import com.example.crm.security.CustomUserDetails;
import com.example.crm.service.SecurityService;
import com.example.crm.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {

    UserRepository userRepository;
    UserService userService;
    SecurityService securityService;

    @GetMapping("/")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        model.addAttribute("user", user);
        String role = user.getRole().getName();
        return switch (role) {
            case "ADMIN" -> "admin/profile";
            case "LEADER" -> "leader/profile";
            case "MEMBER" -> "member/profile";
            case null, default -> "error";
        };
    }

    @PostMapping("/profile")
    public String updateProfile(
            @ModelAttribute("user") User formUser,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        try {
            User updateUser = userService.updateUser(userDetails.getId(), formUser);
            securityService.refreshAuthentication(updateUser);

            redirectAttributes.addFlashAttribute("messages", "Cập nhật thành công!");
            redirectAttributes.addFlashAttribute("type", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("messages", "Cập nhật thất bại: " + e.getMessage());
            redirectAttributes.addFlashAttribute("type", "danger");
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("messages", "Đã xảy ra lỗi không mong muốn.");
            redirectAttributes.addFlashAttribute("type", "danger");
        }
        return "redirect:/profile";
    }
}
