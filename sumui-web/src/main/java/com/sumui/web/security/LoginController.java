package com.sumui.web.security;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sumui.common.annotation.OperateLog;
import com.sumui.common.constants.OperateTypeEnum;
import com.sumui.common.constants.StatusEnum;
import com.sumui.common.model.ReqResult;
import com.sumui.common.model.security.LoginBody;
import com.sumui.service.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

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

    @Value("${weixin.appid}")
    private String wxAppid;
    @Value("${weixin.secret}")
    private String wxSecret;
    @Value("${weixin.url}")
    private String wxUrl;

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

    @GetMapping("/api/wx-login")
    public ReqResult<String> wxLogin(@RequestParam String code) {
        log.info("code:{}",code);
        String url = wxUrl + "&appid=" + wxAppid + "&secret=" + wxSecret + "&js_code=" + code;
        try (HttpResponse response = HttpUtil.createGet(url).execute()) {
            String resp = response.body();
            log.info("resp:{}", resp);
            JSONObject parsedObj = JSONUtil.parseObj(resp);
            if (ObjectUtil.isEmpty(parsedObj)) {
                return ReqResult.fail(StatusEnum.FAIL, "获取用户信息失败");
            }
            String openId = parsedObj.getStr("openid");
            String userId = "";
            if (!StrUtil.isBlank(openId)) {
                // todo 登录成功，设置新用户信息
                return ReqResult.ok(openId);
            }
            return ReqResult.fail("登录失败！");
        }
    }
}
