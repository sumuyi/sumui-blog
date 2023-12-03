package com.sumui;

import cn.dev33.satoken.SaManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description 启动类
 * @Author @Sunl
 * @Date 2023/12/3 13:32
 */
@SpringBootApplication
public class SumuiBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(SumuiBlogApplication.class, args);
        System.out.println("===============================");
        System.out.println("服务启动成功");
        System.out.println("===============================");

        System.out.println("启动成功，Sa-Token 配置如下：" + SaManager.getConfig());
    }
}
