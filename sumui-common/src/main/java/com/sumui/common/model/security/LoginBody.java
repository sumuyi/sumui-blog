package com.sumui.common.model.security;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录实体类
 * @Description
 * @Author @Sunl
 * @Date 2023/12/6 19:49
 */
@Data
public class LoginBody {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    /** 缓存验证码的RedisKey */
//    @NotBlank(message = "验证码标识不能为空")
    private String uuid;

//    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

}
