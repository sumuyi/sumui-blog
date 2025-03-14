package com.sumui.service;

/**
 * 登录业务 接口层
 * @Description
 * @Author @Sunl
 * @Date 2023/12/6 20:07
 */
public interface LoginService {

    /**
     * 登录接口
     * @param username 用户名
     * @param password 密码
     * @return
     */
    String login(String username, String password);
}
