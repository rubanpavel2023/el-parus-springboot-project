package com.example.el_parus_springboot_project.Controllers.AdminController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminLoginController {

    @GetMapping("/adminLogin")
    public String adminLogin() {
        return "Admin/adminLogin";
    }
}

