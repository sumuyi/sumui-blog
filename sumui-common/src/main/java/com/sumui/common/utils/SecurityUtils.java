package com.sumui.common.utils;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.sumui.common.model.system.SysUser;

/**
 * 安全服务工具类
 * 
 * @author sumui
 */
public class SecurityUtils {
    
    /**
     * 获取用户ID
     */
    public static String getUserId() {
        return StpUtil.getLoginIdAsString();
    }
    
    /**
     * 获取用户
     */
    public static SysUser getLoginUser() {
        return (SysUser) StpUtil.getSession().get(SaSession.USER);
    }
    
    /**
     * 获取用户名
     */
    public static String getUsername() {
        SysUser user = getLoginUser();
        return user != null ? user.getUsername() : null;
    }
    
    /**
     * 是否为管理员
     * 
     * @return 结果
     */
    public static boolean isAdmin() {
        SysUser user = getLoginUser();
        return user != null && user.getIsSuper() != null && user.getIsSuper() == 1;
    }
}