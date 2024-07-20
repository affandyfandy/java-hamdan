package com.example.lecture8_assignment3.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Value("${spring.datasource1.driver-class-name}")
    private String ds1DriverClassName;

    @Value("${spring.datasource1.url}")
    private String ds1Url;

    @Value("${spring.datasource1.username}")
    private String ds1Username;

    @Value("${spring.datasource1.password}")
    private String ds1Password;

    @Value("${spring.datasource2.driver-class-name}")
    private String ds2DriverClassName;

    @Value("${spring.datasource2.url}")
    private String ds2Url;

    @Value("${spring.datasource2.username}")
    private String ds2Username;

    @Value("${spring.datasource2.password}")
    private String ds2Password;

    @Bean(name = "datasource1")
    public DataSource dataSource1() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(ds1DriverClassName);
        dataSource.setUrl(ds1Url);
        dataSource.setUsername(ds1Username);
        dataSource.setPassword(ds1Password);
        return dataSource;
    }

    @Bean(name = "datasource2")
    public DataSource dataSource2() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(ds2DriverClassName);
        dataSource.setUrl(ds2Url);
        dataSource.setUsername(ds2Username);
        dataSource.setPassword(ds2Password);
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
