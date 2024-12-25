package com.sumui.web.security;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaTokenInfo;
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
import com.sumui.common.model.security.LoginUserVO;
import com.sumui.common.model.system.SysUser;
import com.sumui.common.utils.uuid.IDUtils;
import com.sumui.service.service.LoginService;
import com.sumui.service.service.system.SysUserService;
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
    @Value("${weixin.access_token_url}")
    private String accessTokenUrl;

    @Resource
    private LoginService loginService;

    @Resource
    private SysUserService userService;

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
    public ReqResult<Object> wxLogin(@RequestParam String code) {
        log.info("code:{}",code);
        String url = wxUrl + "&appid=" + wxAppid + "&secret=" + wxSecret + "&js_code=" + code;
        JSONObject parsedObj;
        try (HttpResponse response = HttpUtil.createGet(url).execute()) {
            String resp = response.body();
            log.info("resp:{}", resp);
            parsedObj = JSONUtil.parseObj(resp);
            if (ObjectUtil.isEmpty(parsedObj)) {
                return ReqResult.fail(StatusEnum.FAIL, "获取用户信息失败");
            }
        }

        String openId = parsedObj.getStr("openid");
        if (StrUtil.isBlank(openId)) {
            return ReqResult.fail("登录失败！");
        }

//        String accessTokenUrl1 = accessTokenUrl + "&appid=" + wxAppid + "&secret=" + wxSecret;
//        JSONObject accessTokenJSON;
//        try (HttpResponse accessTokenRes = HttpUtil.createGet(accessTokenUrl1).execute()) {
//            String resp = accessTokenRes.body();
//            log.info("accessTokenRes:{}", accessTokenRes);
//            accessTokenJSON = JSONUtil.parseObj(accessTokenRes);
//            if (ObjectUtil.isEmpty(accessTokenJSON)) {
//                return ReqResult.fail(StatusEnum.FAIL, "获取用户信息失败");
//            }
//        }
//
//        String accessToken = accessTokenJSON.getStr("access_token");
//        if (StrUtil.isBlank(accessToken)) {
//            return ReqResult.fail("登录失败！");
//        }

        // todo 向数据库查询用户信息，如果不存在则创建新用户
        SysUser user = userService.lambdaQuery().eq(SysUser::getOpenId, openId).one();
        if (user == null) {
            user = new SysUser();
            user.setId(IDUtils.nextId());
            user.setOpenId(openId);
            userService.save(user);
        }
//        userService.wxUserInfo(accessToken,openId);
        StpUtil.login(user.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 构建返回值
        return ReqResult.ok(LoginUserVO.builder()
                .userId(tokenInfo.getLoginId().toString())
                .saToken(tokenInfo.getTokenValue())
                .expireTime(tokenInfo.getTokenTimeout())
                .build()
        );
    }

    // 查询登录状态  ---- http://localhost:8081/acc/isLogin
    @GetMapping("isLogin")
    public ReqResult<String> isLogin() {
        return ReqResult.ok("是否登录：" + StpUtil.isLogin());
    }

    // 查询 Token 信息  ---- http://localhost:8081/acc/tokenInfo
    @GetMapping("tokenInfo")
    public ReqResult<Object> tokenInfo() {
        return ReqResult.ok(StpUtil.getTokenInfo());
    }
}
