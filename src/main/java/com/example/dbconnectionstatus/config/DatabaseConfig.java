package com.example.dbconnectionstatus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Autowired
    private Environment env;

    @Primary
    @Bean(name = "primaryDataSource")
    public DataSource primaryDataSource() {
        String profile = env.getActiveProfiles().length > 0 ? env.getActiveProfiles()[0] : "sit";
        
        if ("uat".equals(profile)) {
            return DataSourceBuilder.create()
                .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .url("jdbc:sqlserver://172.20.15.84;trustServerCertificate=true")
                .username("portalusr")
                .password("portal@usr")
                .build();
        } else {
            return DataSourceBuilder.create()
                .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .url("jdbc:sqlserver://172.20.17.48;trustServerCertificate=true")
                .username("portalusr")
                .password("portal@usr")
                .build();
        }
    }

    @Bean(name = "db2BosprodDataSource")
    public DataSource db2BosprodDataSource() {
        String profile = env.getActiveProfiles().length > 0 ? env.getActiveProfiles()[0] : "sit";
        
        if ("uat".equals(profile)) {
            return DataSourceBuilder.create()
                .driverClassName("com.ibm.db2.jcc.DB2Driver")
                .url("jdbc:db2://172.20.15.52:50000/BOSPROD")
                .username("cardpro")
                .password("sacombank@123456789")
                .build();
        } else {
            return DataSourceBuilder.create()
                .driverClassName("com.ibm.db2.jcc.DB2Driver")
                .url("jdbc:db2://172.20.17.21:50000/BOSPROD")
                .username("cardpro")
                .password("sacombank@123456789")
                .build();
        }
    }

    @Bean(name = "db2FepDataSource")
    public DataSource db2FepDataSource() {
        String profile = env.getActiveProfiles().length > 0 ? env.getActiveProfiles()[0] : "sit";
        
        if ("uat".equals(profile)) {
            return DataSourceBuilder.create()
                .driverClassName("com.ibm.db2.jcc.DB2Driver")
                .url("jdbc:db2://172.20.15.52:50000/FEPPROD")
                .username("cardpro")
                .password("cardpro")
                .build();
        } else {
            return DataSourceBuilder.create()
                .driverClassName("com.ibm.db2.jcc.DB2Driver")
                .url("jdbc:db2://172.20.17.21:50000/FEPPROD")
                .username("cardpro")
                .password("sacombank@123456789")
                .build();
        }
    }
}