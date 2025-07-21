package com.example.dbconnectionstatus.model;

import java.time.LocalDateTime;

public class ConnectionStatus {
    private String name;
    private String type;
    private String host;
    private String database;
    private String username;
    private boolean connected;
    private String status;
    private String errorMessage;
    private LocalDateTime lastChecked;
    private long responseTimeMs;

    public ConnectionStatus() {
    }

    public ConnectionStatus(String name, String type, String host, String database, String username) {
        this.name = name;
        this.type = type;
        this.host = host;
        this.database = database;
        this.username = username;
        this.lastChecked = LocalDateTime.now();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        this.status = connected ? "CONNECTED" : "DISCONNECTED";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(LocalDateTime lastChecked) {
        this.lastChecked = lastChecked;
    }

    public long getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(long responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }
}