package com.project.back_end.mvc;

import com.project.back_end.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class DashboardController {

    private final TokenService tokenService;

    @Autowired
    public DashboardController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable("token") String token) {
        Map<String, String> result = tokenService.validateToken(token, "admin");
        if (result == null || result.isEmpty()) {
            return "admin/adminDashboard";
        }
        return "redirect:/";
    }

    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable("token") String token) {
        Map<String, String> result = tokenService.validateToken(token, "doctor");
        if (result == null || result.isEmpty()) {
            return "doctor/doctorDashboard";
        }
        return "redirect:/";
    }

}
