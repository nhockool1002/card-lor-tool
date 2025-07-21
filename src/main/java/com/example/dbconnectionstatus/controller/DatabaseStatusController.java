package com.example.dbconnectionstatus.controller;

import com.example.dbconnectionstatus.model.ConnectionStatus;
import com.example.dbconnectionstatus.service.DatabaseHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DatabaseStatusController {

    @Autowired
    private DatabaseHealthService databaseHealthService;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getAllDatabaseStatus(
            @RequestParam(value = "env", defaultValue = "ALL") String environment) {
        List<ConnectionStatus> statuses = databaseHealthService.getConnectionStatusesByEnvironment(environment);
        Map<String, Long> summary = databaseHealthService.getEnvironmentSummary();
        
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("selectedEnvironment", environment);
        response.put("databases", statuses);
        response.put("totalDatabases", statuses.size());
        response.put("connectedDatabases", statuses.stream().mapToLong(s -> s.isConnected() ? 1 : 0).sum());
        response.put("environmentSummary", summary);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealthCheck() {
        List<ConnectionStatus> statuses = databaseHealthService.getAllConnectionStatuses();
        Map<String, Long> summary = databaseHealthService.getEnvironmentSummary();
        
        boolean allConnected = statuses.stream().allMatch(ConnectionStatus::isConnected);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", allConnected ? "UP" : "DOWN");
        response.put("timestamp", System.currentTimeMillis());
        response.put("details", statuses);
        response.put("environmentSummary", summary);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getEnvironmentSummary() {
        Map<String, Long> summary = databaseHealthService.getEnvironmentSummary();
        return ResponseEntity.ok(summary);
    }
}