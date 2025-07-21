package com.example.dbconnectionstatus.controller;

import com.example.dbconnectionstatus.service.DatabaseHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Autowired
    private DatabaseHealthService databaseHealthService;

    @Autowired
    private Environment environment;

    @GetMapping("/")
    public String dashboard(Model model) {
        String activeProfile = environment.getActiveProfiles().length > 0 ? 
            environment.getActiveProfiles()[0] : "sit";
        
        model.addAttribute("connectionStatuses", databaseHealthService.getAllConnectionStatuses());
        model.addAttribute("environment", activeProfile.toUpperCase());
        model.addAttribute("refreshTime", java.time.LocalDateTime.now());
        
        return "dashboard";
    }
}