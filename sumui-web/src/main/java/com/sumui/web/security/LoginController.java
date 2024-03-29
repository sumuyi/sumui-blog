package com.sumui.web.security;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.sumui.common.annotation.OperateLog;
import com.sumui.common.constants.OperateTypeEnum;
import com.sumui.common.model.ReqResult;
import com.sumui.common.model.security.LoginBody;
import com.sumui.service.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 登录入口
 * @Description
 * @Author @Sunl
 * @Date 2023/12/6 19:49
 */
@Slf4j
@RequestMapping("/auth")
@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

//    @OperateLog(title = "登录", businessType = OperateTypeEnum.LOGIN, excludeParamNames = { "password" })
    @PostMapping("/login")
    public ReqResult<String> login(@Validated @RequestBody LoginBody loginBody) {
        return ReqResult.ok(loginService.login(loginBody.getUsername(), loginBody.getPassword()));
    }

    @PostMapping("/logout")
    public ReqResult<String> logout() {
        StpUtil.logout();
        return ReqResult.ok("登出成功！");
    }
}
