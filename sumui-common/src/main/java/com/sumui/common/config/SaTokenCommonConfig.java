package com.sumui.common.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.sumui.common.model.system.SysUser;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token 全局通用配置
 * 
 * @author sumui
 */
@Configuration
public class SaTokenCommonConfig {
    
    /**
     * 获取当前登录用户ID
     * 
     * @return 用户ID
     */
    public static String getCurrentUserId() {
        return StpUtil.getLoginIdAsString();
    }
    
    /**
     * 获取当前登录用户信息
     * 
     * @return 当前登录用户信息
     */
    public static SysUser getCurrentUser() {
        return (SysUser) StpUtil.getSession().get(SaSession.USER);
    }
    
    /**
     * 判断当前是否已登录
     * 
     * @return 是否已登录
     */
    public static boolean isLogin() {
        return StpUtil.isLogin();
    }
}