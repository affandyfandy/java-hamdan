package com.example.lecture8_assignment3.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Bean(name = "datasource1")
    @ConfigurationProperties(prefix = "spring.datasource1")
    public DataSource dataSource1() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/employee_ass2");
        dataSource.setUsername("root");
        dataSource.setPassword("dayak1352");
        return dataSource;
    }

    @Bean(name = "datasource2")
    @ConfigurationProperties(prefix = "spring.datasource2")
    public DataSource dataSource2() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/employee_ass2_second");
        dataSource.setUsername("root");
        dataSource.setPassword("dayak1352");
        return dataSource;
    }

    @Bean(name = "jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1(@Qualifier("datasource1") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean(name = "jdbcTemplate2")
    public JdbcTemplate jdbcTemplate2(@Qualifier("datasource2") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean(name = "transactionManager1")
    public PlatformTransactionManager transactionManager1(@Qualifier("datasource1") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Bean(name = "transactionManager2")
    public PlatformTransactionManager transactionManager2(@Qualifier("datasource2") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }
}
