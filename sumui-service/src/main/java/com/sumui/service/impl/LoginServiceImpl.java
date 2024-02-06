package com.sumui.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.sumui.common.constants.StatusEnum;
import com.sumui.common.constants.UserStatus;
import com.sumui.common.exception.UserException;
import com.sumui.common.model.system.SysUser;
import com.sumui.service.service.LoginService;
import com.sumui.service.service.system.SysUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description
 * @Author @Sunl
 * @Date 2023/12/6 20:22
 */
@Service
@Log4j2
public class LoginServiceImpl implements LoginService {
    @Resource
    private SysUserService userService;
    /**
     * 登录接口
     *
     * @param username   用户名
     * @param password   密码
     * @return
     */
    @Override
    public String login(String username, String password) {
        // 校验
        log.error(username + "----" + password);
        // 获取数据库账号信息
        SysUser loginInfo = this.validLoginInfo(username, password);
        StpUtil.login(loginInfo.getId());
        return StpUtil.getTokenValue();
    }

    private SysUser validLoginInfo(String username, String password) {
        // 用户名或密码为空 错误
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            throw new UserException(StatusEnum.USER_OR_PWD_EMPTY);
        }

        // todo 用户名不在指定范围内 错误

        // 获取用户信息
        SysUser user = userService.getUserInfoByUserName(username);
        if (user == null){
            throw new UserException(StatusEnum.USER_NOT_EXISTS);
        }

        if (UserStatus.DELETED.getCode().equals(user.getStatus().toString())) {
            throw new UserException(StatusEnum.USER_DELETE);
        }

        if (UserStatus.DISABLE.getCode().equals(user.getStatus().toString())) {
            throw new UserException(StatusEnum.USER_DISABLE);
        }

        // 验证密码

        // 加密
        String aesEncryptPwd = SaSecureUtil.aesEncrypt(user.getSalt(), password);
        // 比较
        if (!aesEncryptPwd.equals(user.getPassword())){
            throw new UserException(StatusEnum.USER_PWD_ERROR);
        }
        return user;
    }
}
