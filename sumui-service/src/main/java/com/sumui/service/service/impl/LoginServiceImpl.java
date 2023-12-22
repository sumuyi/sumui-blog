package com.sumui.service.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sumui.common.model.system.SysUser;
import com.sumui.service.service.LoginService;
import com.sumui.service.service.SysUserService;
import com.sumui.service.service.system.SysOperLogService;
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
        this.validLoginInfo();
        // 获取数据库账号信息

        return "ok";
    }

    private void validLoginInfo() {
    }

    private SysUser getAccount(String username) {
//        SysUser user = SysUserService.getOne(new LambdaQueryWrapper<SysUser>()
//                .eq(SysUser::getUsername, username)
//                .last("limit 1")
//        );
//        Assert.notNull(user, "用户名或密码错误");
        return new SysUser();
    }
}
