package com.sumui;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description 启动类
 * @Author @Sunl
 * @Date 2023/12/3 13:32
 */
@SpringBootApplication(scanBasePackages = {"com.sumui"})
@ComponentScan(basePackages = {"com.sumui.service.convert", "com.sumui"})
public class SumuiBlogApplication {
    public static void main(String[] args) {
        // 使用SpringApplicationBuilder设置banner模式
        new SpringApplicationBuilder(SumuiBlogApplication.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
    }
}
