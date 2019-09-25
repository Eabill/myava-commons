package com.myava.mybatis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * MyBatis mapper扫描器配置
 *
 * @author biao
 */
@Configuration
public class MyBatisMapperScannerConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer( ) {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.myava.**.mapper");
        Properties propertiesMapper = new Properties();
        // 通用mapper位置，不要和其他mapper、dao放在同一个目录
        propertiesMapper.setProperty("mappers", "com.myava.mybatis.common.BaseMapper");
        propertiesMapper.setProperty("notEmpty", "false");
        // 主键回写方法执行顺序，默认AFTER，可选值为(BEFORE|AFTER)
        propertiesMapper.setProperty("ORDER", "AFTER");
        propertiesMapper.setProperty("map-underscore-to-camel-case", "true");
        mapperScannerConfigurer.setProperties(propertiesMapper);
        return mapperScannerConfigurer;
    }
}
