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

    // SIT Environment DataSources
    @Primary
    @Bean(name = "sitMssqlDataSource")
    public DataSource sitMssqlDataSource() {
        return DataSourceBuilder.create()
            .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
            .url("jdbc:sqlserver://172.20.17.48;trustServerCertificate=true")
            .username("portalusr")
            .password("portal@usr")
            .build();
    }

    @Bean(name = "sitDb2BosprodDataSource")
    public DataSource sitDb2BosprodDataSource() {
        return DataSourceBuilder.create()
            .driverClassName("com.ibm.db2.jcc.DB2Driver")
            .url("jdbc:db2://172.20.17.21:50000/BOSPROD")
            .username("cardpro")
            .password("sacombank@123456789")
            .build();
    }

    @Bean(name = "sitDb2FepDataSource")
    public DataSource sitDb2FepDataSource() {
        return DataSourceBuilder.create()
            .driverClassName("com.ibm.db2.jcc.DB2Driver")
            .url("jdbc:db2://172.20.17.21:50000/FEPPROD")
            .username("cardpro")
            .password("sacombank@123456789")
            .build();
    }

    // UAT Environment DataSources
    @Bean(name = "uatMssqlDataSource")
    public DataSource uatMssqlDataSource() {
        return DataSourceBuilder.create()
            .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
            .url("jdbc:sqlserver://172.20.15.84;trustServerCertificate=true")
            .username("portalusr")
            .password("portal@usr")
            .build();
    }

    @Bean(name = "uatDb2BosprodDataSource")
    public DataSource uatDb2BosprodDataSource() {
        return DataSourceBuilder.create()
            .driverClassName("com.ibm.db2.jcc.DB2Driver")
            .url("jdbc:db2://172.20.15.52:50000/BOSPROD")
            .username("cardpro")
            .password("sacombank@123456789")
            .build();
    }

    @Bean(name = "uatDb2FepDataSource")
    public DataSource uatDb2FepDataSource() {
        return DataSourceBuilder.create()
            .driverClassName("com.ibm.db2.jcc.DB2Driver")
            .url("jdbc:db2://172.20.15.52:50000/FEPPROD")
            .username("cardpro")
            .password("cardpro")
            .build();
    }
}