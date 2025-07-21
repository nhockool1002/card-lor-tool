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

@Service
public class DatabaseHealthService {

    @Autowired
    @Qualifier("primaryDataSource")
    private DataSource primaryDataSource;

    @Autowired
    @Qualifier("db2BosprodDataSource")
    private DataSource db2BosprodDataSource;

    @Autowired
    @Qualifier("db2FepDataSource")
    private DataSource db2FepDataSource;

    @Autowired
    private Environment environment;

    public List<ConnectionStatus> getAllConnectionStatuses() {
        List<ConnectionStatus> statuses = new ArrayList<>();
        
        String activeProfile = environment.getActiveProfiles().length > 0 ? 
            environment.getActiveProfiles()[0] : "sit";
        
        // Check MSSQL Primary
        statuses.add(checkConnection("MSSQL Primary (" + activeProfile.toUpperCase() + ")", 
            "MSSQL", getMSSQLHost(activeProfile), "", getMSSQLUsername(), primaryDataSource));
        
        // Check DB2 BOSPROD
        statuses.add(checkConnection("DB2 BOSPROD (" + activeProfile.toUpperCase() + ")", 
            "DB2", getDB2Host(activeProfile), "BOSPROD", getDB2Username(), db2BosprodDataSource));
        
        // Check DB2 FEP
        statuses.add(checkConnection("DB2 FEP (" + activeProfile.toUpperCase() + ")", 
            "DB2", getDB2Host(activeProfile), "FEPPROD", getDB2FepUsername(activeProfile), db2FepDataSource));
        
        return statuses;
    }

    private ConnectionStatus checkConnection(String name, String type, String host, 
                                           String database, String username, DataSource dataSource) {
        ConnectionStatus status = new ConnectionStatus(name, type, host, database, username);
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

    private String getMSSQLHost(String profile) {
        return "sit".equals(profile) ? "172.20.17.48" : "172.20.15.84";
    }

    private String getDB2Host(String profile) {
        return "sit".equals(profile) ? "172.20.17.21" : "172.20.15.52";
    }

    private String getMSSQLUsername() {
        return "portalusr";
    }

    private String getDB2Username() {
        return "cardpro";
    }

    private String getDB2FepUsername(String profile) {
        // UAT FEP có username khác với BOSPROD
        return "cardpro";
    }
}