package com.sumui.config.satoken;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.interceptor.SaInterceptor;
//import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * sa-token 配置
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Slf4j
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    /**
     * 注册sa-token的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 注册路由拦截器，自定义验证规则
        registry.addInterceptor(new SaInterceptor(handler -> {
                    // 登录验证 -- 排除多个路径
                    // 检查是否登录 是否有token
                    SaRouter
                            // 获取所有的
                            .match("/**")
                            .notMatch("/auth/**","/api/files/**")             // 排除认证相关路径
                            .notMatch("/doc.html")            // 排除文档接口
                            .notMatch("/webjars/**")          // 排除静态资源
                            .notMatch("/swagger-resources")   // 排除 Swagger 资源
                            .check(StpUtil::checkLogin);
                })).addPathPatterns("/**");
    }

//    @Bean
//    public StpLogic getStpLogicJwt() {
//        // Sa-Token 整合 jwt (简单模式)
//        return new StpLogicJwtForSimple();
//    }

    /**
     * 跨域配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置全局跨域
        registry.addMapping("/**")
            // 允许所有来源（生产环境建议配置具体域名）
            .allowedOriginPatterns("*")
            // 允许的请求方法
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            // 允许的请求头
            .allowedHeaders("*")
            // 有效时间（单位：秒）
            .maxAge(3600)
            // 允许携带凭证（如需要cookie验证要设置为true）
            .allowCredentials(true);
    }

}
