package com.example.test.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.example.test.utils.ModuloDatabaseShardingAlgorithm;
import com.example.test.utils.ModuloTableShardingAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceShardingConfig {

//    @Bean
//    public DataSource getDataSource() {
//        return buildDataSource();
//    }
//
//    private DataSource buildDataSource() {
//        //设置分库映射
//        Map<String, DataSource> dataSourceMap = new HashMap<>(2);
//        //添加两个数据库dn1,dn2到map里
//        dataSourceMap.put("dn1", createDataSource("dn1"));
//        dataSourceMap.put("dn2", createDataSource("dn2"));
//        //设置默认db为dn1，也就是为那些没有配置分库分表策略的指定的默认库
//        //如果只有一个库，也就是不需要分库的话，map里只放一个映射就行了，只有一个库时不需要指定默认库，但2个及以上时必须指定默认库，否则那些没有配置策略的表将无法操作数据
//        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap, "dn1");
//
//        //设置分表映射，将t_order_0和t_order_1两个实际的表映射到t_order逻辑表
//        //0和1两个表是真实的表，t_order是个虚拟不存在的表，只是供使用。如查询所有数据就是select * from t_order就能查完0和1表的
//        TableRule orderTableRule = TableRule.builder("t_order")
//                .actualTables(Arrays.asList("t_order_0", "t_order_1"))
//                .dataSourceRule(dataSourceRule)
//                .build();
//
//        //具体分库分表策略，按什么规则来分
//        ShardingRule shardingRule = ShardingRule.builder()
//                .dataSourceRule(dataSourceRule)
//                .tableRules(Arrays.asList(orderTableRule))
//                .databaseShardingStrategy(new DatabaseShardingStrategy("orderId", new ModuloDatabaseShardingAlgorithm()))
//                .tableShardingStrategy(new TableShardingStrategy("id", new ModuloTableShardingAlgorithm())).build();
//
//        DataSource dataSource = null;
//        try {
//            dataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return dataSource;
//    }
//
//    private static DataSource createDataSource(final String dataSourceName) {
//        //使用druid连接数据库
//        DruidDataSource result = new DruidDataSource();
//        result.setDriverClassName("com.mysql.jdbc.Driver");
//        result.setUrl("jdbc:mysql://localhost:3306/" + dataSourceName + "?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&zeroDateTimeBehavior=convertToNull");
//        result.setUsername("root");
//        result.setPassword("1111a!");
//        return result;
//    }

}
