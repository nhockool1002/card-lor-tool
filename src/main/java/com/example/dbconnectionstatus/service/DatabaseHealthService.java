package com.example.dbconnectionstatus.service;

import com.example.dbconnectionstatus.model.ConnectionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DatabaseHealthService {

    // SIT DataSources
    @Autowired
    @Qualifier("sitMssqlDataSource")
    private DataSource sitMssqlDataSource;

    @Autowired
    @Qualifier("sitDb2BosprodDataSource")
    private DataSource sitDb2BosprodDataSource;

    @Autowired
    @Qualifier("sitDb2FepDataSource")
    private DataSource sitDb2FepDataSource;

    // UAT DataSources
    @Autowired
    @Qualifier("uatMssqlDataSource")
    private DataSource uatMssqlDataSource;

    @Autowired
    @Qualifier("uatDb2BosprodDataSource")
    private DataSource uatDb2BosprodDataSource;

    @Autowired
    @Qualifier("uatDb2FepDataSource")
    private DataSource uatDb2FepDataSource;

    @Autowired
    private Environment environment;

    public List<ConnectionStatus> getAllConnectionStatuses() {
        List<ConnectionStatus> statuses = new ArrayList<>();
        
        // Check SIT Databases
        statuses.add(checkConnection("MSSQL Primary (SIT)", 
            "MSSQL", "172.20.17.48", "", "portalusr", sitMssqlDataSource, "SIT"));
        
        statuses.add(checkConnection("DB2 BOSPROD (SIT)", 
            "DB2", "172.20.17.21", "BOSPROD", "cardpro", sitDb2BosprodDataSource, "SIT"));
        
        statuses.add(checkConnection("DB2 FEP (SIT)", 
            "DB2", "172.20.17.21", "FEPPROD", "cardpro", sitDb2FepDataSource, "SIT"));
        
        // Check UAT Databases
        statuses.add(checkConnection("MSSQL Primary (UAT)", 
            "MSSQL", "172.20.15.84", "", "portalusr", uatMssqlDataSource, "UAT"));
        
        statuses.add(checkConnection("DB2 BOSPROD (UAT)", 
            "DB2", "172.20.15.52", "BOSPROD", "cardpro", uatDb2BosprodDataSource, "UAT"));
        
        statuses.add(checkConnection("DB2 FEP (UAT)", 
            "DB2", "172.20.15.52", "FEPPROD", "cardpro", uatDb2FepDataSource, "UAT"));
        
        return statuses;
    }

    public List<ConnectionStatus> getConnectionStatusesByEnvironment(String environment) {
        List<ConnectionStatus> allStatuses = getAllConnectionStatuses();
        if (environment == null || environment.equalsIgnoreCase("ALL")) {
            return allStatuses;
        }
        
        return allStatuses.stream()
                .filter(status -> status.getEnvironment().equalsIgnoreCase(environment))
                .collect(Collectors.toList());
    }

    public Map<String, Long> getEnvironmentSummary() {
        List<ConnectionStatus> allStatuses = getAllConnectionStatuses();
        
        Map<String, Long> sitSummary = allStatuses.stream()
                .filter(status -> "SIT".equals(status.getEnvironment()))
                .collect(Collectors.groupingBy(
                    status -> status.isConnected() ? "connected" : "disconnected",
                    Collectors.counting()
                ));

        Map<String, Long> uatSummary = allStatuses.stream()
                .filter(status -> "UAT".equals(status.getEnvironment()))
                .collect(Collectors.groupingBy(
                    status -> status.isConnected() ? "connected" : "disconnected",
                    Collectors.counting()
                ));

        Map<String, Long> summary = new java.util.HashMap<>();
        summary.put("sitConnected", sitSummary.getOrDefault("connected", 0L));
        summary.put("sitDisconnected", sitSummary.getOrDefault("disconnected", 0L));
        summary.put("uatConnected", uatSummary.getOrDefault("connected", 0L));
        summary.put("uatDisconnected", uatSummary.getOrDefault("disconnected", 0L));
        summary.put("totalConnected", sitSummary.getOrDefault("connected", 0L) + uatSummary.getOrDefault("connected", 0L));
        summary.put("totalDisconnected", sitSummary.getOrDefault("disconnected", 0L) + uatSummary.getOrDefault("disconnected", 0L));
        
        return summary;
    }

    private ConnectionStatus checkConnection(String name, String type, String host, 
                                           String database, String username, DataSource dataSource, String environment) {
        ConnectionStatus status = new ConnectionStatus(name, type, host, database, username);
        status.setEnvironment(environment);
        long startTime = System.currentTimeMillis();
        
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                // Test với một query đơn giản
                String testQuery = getTestQuery(type);
                try (PreparedStatement stmt = connection.prepareStatement(testQuery);
                     ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        status.setConnected(true);
                        status.setErrorMessage(null);
                    }
                }
            }
        } catch (Exception e) {
            status.setConnected(false);
            status.setErrorMessage(e.getMessage());
        }
        
        long endTime = System.currentTimeMillis();
        status.setResponseTimeMs(endTime - startTime);
        status.setLastChecked(LocalDateTime.now());
        
        return status;
    }

    private String getTestQuery(String dbType) {
        switch (dbType.toUpperCase()) {
            case "MSSQL":
                return "SELECT 1";
            case "DB2":
                return "SELECT 1 FROM SYSIBM.SYSDUMMY1";
            default:
                return "SELECT 1";
        }
    }
}