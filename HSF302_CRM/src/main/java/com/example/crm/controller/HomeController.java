package com.example.crm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        var authorities = authentication.getAuthorities();
        for (var a : authorities) {
            if (a.getAuthority().equals("ADMIN") || a.getAuthority().equals("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard";
            }
            if (a.getAuthority().equals("LEADER") || a.getAuthority().equals("ROLE_LEADER")) {
                return "redirect:/leader/dashboard";
            }
            if (a.getAuthority().equals("MEMBER") || a.getAuthority().equals("ROLE_MEMBER")) {
                return "redirect:/member/dashboard";
            }
        }
        return "redirect:/login";
    }
}
