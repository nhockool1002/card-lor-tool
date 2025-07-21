package com.example.dbconnectionstatus.controller;

import com.example.dbconnectionstatus.service.DatabaseHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class WebController {

    @Autowired
    private DatabaseHealthService databaseHealthService;

    @Autowired
    private Environment environment;

    @GetMapping("/")
    public String dashboard(Model model, @RequestParam(value = "env", defaultValue = "ALL") String selectedEnvironment) {
        // Get connection statuses based on environment filter
        model.addAttribute("connectionStatuses", 
            databaseHealthService.getConnectionStatusesByEnvironment(selectedEnvironment));
        
        // Get environment summary for status bar
        Map<String, Long> summary = databaseHealthService.getEnvironmentSummary();
        model.addAttribute("environmentSummary", summary);
        
        // Add current selection and refresh time
        model.addAttribute("selectedEnvironment", selectedEnvironment);
        model.addAttribute("refreshTime", java.time.LocalDateTime.now());
        
        return "dashboard";
    }
}