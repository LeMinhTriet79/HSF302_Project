package com.example.crm.config;

import com.example.crm.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModeAttributes {
    @ModelAttribute
    public void addUserInfo(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails user) {
            model.addAttribute("currentUserName", user.getFullName());
            model.addAttribute("currentUserRole", user.getRole());
            model.addAttribute("currentUserId", user.getId());
        }
    }
}
