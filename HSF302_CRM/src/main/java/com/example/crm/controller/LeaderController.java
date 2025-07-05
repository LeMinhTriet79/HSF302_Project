package com.example.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/leader")
public class LeaderController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "leader/dashboard"; // leader/dashboard.html ở /templates/
    }
}