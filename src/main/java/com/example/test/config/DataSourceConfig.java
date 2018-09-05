package com.example.test.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @Author:
 * @Description:
 * @Date: 2018-08-29
 */
@Configuration
public class DataSourceConfig {

    /**
     * 加载数据源
     *
     * @return
     */
    @Bean(name = "primaryDataSource")
    @Qualifier(value = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 加载数据源
     *
     * @return
     */
    @Bean(name = "secondDataSource")
    @Qualifier(value = "secondDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.second")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 初始化JdbcTemplate
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier(value = "primaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * 初始化JdbcTemplate
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "secondJdbcTemplate")
    public JdbcTemplate secondJdbcTemplate(@Qualifier(value = "secondDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
