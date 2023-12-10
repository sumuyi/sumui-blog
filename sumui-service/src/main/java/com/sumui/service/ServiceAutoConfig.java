package com.sumui.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author @Sunl
 * @Date 2023/12/10 15:35
 */
@Configuration
@ComponentScan("com.sumui.dao")
@MapperScan(
        basePackages = { "com.sumui.dao.mapper" }
)
public class ServiceAutoConfig {
}
