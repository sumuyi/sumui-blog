package com.sumui.web.security;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sumui.common.model.ReqResult;
import com.sumui.common.model.constants.WXConstant;
import com.sumui.common.model.security.LoginBody;
import com.sumui.common.model.security.LoginUserVO;
import com.sumui.common.model.security.WxLoginDTO;
import com.sumui.common.model.system.SysUser;
import com.sumui.common.utils.uuid.IDUtils;
import com.sumui.common.utils.wechat.WechatUtil;
import com.sumui.service.LoginService;
import com.sumui.service.impl.system.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private WXConstant wxConstant;

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

    @PostMapping("/api/wx-login")
    public ReqResult<Object> wxLogin(@RequestBody WxLoginDTO loginDTO) {
        log.info("loginDTO:{}",loginDTO);
        // 用户非敏感信息：rawData
        // 签名：signature
        JSONObject rawDataJson = JSONUtil.parseObj(loginDTO.getRawData());
        // 1.接收小程序发送的code
        log.info("wxConstant:{}", wxConstant);
        // 2.开发者服务器 登录凭证校验接口 appid + appSecret + code
        JSONObject sessionKeyOrOpenId = WechatUtil.getSessionKeyOrOpenId(wxConstant, loginDTO.getCode());
        // 3.接收微信接口服务 获取返回的参数
        String openId = sessionKeyOrOpenId.getStr("openid");
        String sessionKey = sessionKeyOrOpenId.getStr("session_key");

        // 4.校验签名 小程序发送的签名signature与服务器端生成的签名signature2 = sha1(rawData + sessionKey)
        String signature2 = DigestUtils.sha1Hex(loginDTO.getRawData() + sessionKey);
        if (!loginDTO.getSignature().equals(signature2)) {
            return ReqResult.fail("签名校验失败");
        }

        // // 5.根据返回的User实体类，判断用户是否是新用户，是的话，将用户信息存到数据库；
        SysUser user = userService.lambdaQuery().eq(SysUser::getOpenId, openId).one();
        if (user == null) {
            user = new SysUser();
            user.setId(IDUtils.nextId());
            user.setOpenId(openId);
            user.setNickname(rawDataJson.getStr("nickName"));
            user.setAvatar(rawDataJson.getStr("avatarUrl"));
            user.setPassword(BCrypt.hashpw("123456"));
            userService.save(user);
        }
//        userService.wxUserInfo(accessToken,openId);
        StpUtil.login(user.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 构建返回值
        return ReqResult.ok(LoginUserVO.builder()
                .userId(tokenInfo.getLoginId().toString())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .userName(user.getUsername())
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
