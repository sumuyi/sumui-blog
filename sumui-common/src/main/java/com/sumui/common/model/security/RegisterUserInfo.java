package com.sumui.common.model.security;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 注册用户信息
 * @Author @Sunl
 * @Date 2023/12/25 19:38
 */
@Data
public class RegisterUserInfo implements Serializable {
    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /** 二次验证密码 */
    private String confirmPwd;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private Integer sex;
}
