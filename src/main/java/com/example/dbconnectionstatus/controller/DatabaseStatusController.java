package com.example.dbconnectionstatus.controller;

import com.example.dbconnectionstatus.model.ConnectionStatus;
import com.example.dbconnectionstatus.service.DatabaseHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<Map<String, Object>> getAllDatabaseStatus() {
        List<ConnectionStatus> statuses = databaseHealthService.getAllConnectionStatuses();
        
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("databases", statuses);
        response.put("totalDatabases", statuses.size());
        response.put("connectedDatabases", statuses.stream().mapToLong(s -> s.isConnected() ? 1 : 0).sum());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealthCheck() {
        List<ConnectionStatus> statuses = databaseHealthService.getAllConnectionStatuses();
        
        boolean allConnected = statuses.stream().allMatch(ConnectionStatus::isConnected);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", allConnected ? "UP" : "DOWN");
        response.put("timestamp", System.currentTimeMillis());
        response.put("details", statuses);
        
        return ResponseEntity.ok(response);
    }
}