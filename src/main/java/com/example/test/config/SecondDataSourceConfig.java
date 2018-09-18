package com.example.test.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan(basePackages = "com.example.test.mapper",sqlSessionTemplateRef = "secondSqlSessionTemplate")
public class SecondDataSourceConfig {

//    /**
//     * 加载主数据源----------------------------------------------------------------------------------
//     *
//     * @return
//     */
//    @Bean(name = "primaryDataSource")
//    @Qualifier(value = "primaryDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.primary")
//    public DataSource primaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    /**
//     * primaryJdbcTemplate
//     *
//     * @param dataSource
//     * @return
//     */
//    @Bean(name = "primaryJdbcTemplate")
//    public JdbcTemplate primaryJdbcTemplate(@Qualifier(value = "primaryDataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }

    /**
     * 加载second数据源----------------------------------------------------------------------------------
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
     * secondJdbcTemplate
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "secondJdbcTemplate")
    public JdbcTemplate secondJdbcTemplate(@Qualifier(value = "secondDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "outerSqlSessionFactory")
    public SqlSessionFactory outerSqlSessionFactory(@Qualifier("secondDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/outer/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "secondSqlSessionTemplate")
    public SqlSessionTemplate outerSqlSessionTemplate(@Qualifier("outerSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

//    /**
//     * 加载myCat数据源----------------------------------------------------------------------------------
//     *
//     * @return
//     */
//    @Bean(name = "myCatDataSource")
//    @Qualifier(value = "myCatDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.third")
//    public DataSource myCatDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    /**
//     * myCatJdbcTemplate
//     *
//     * @param dataSource
//     * @return
//     */
//    @Bean(name = "myCatJdbcTemplate")
//    public JdbcTemplate myCatJdbcTemplate(@Qualifier(value = "myCatDataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
}
