package com.sumui.service.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sumui.common.model.system.SysUser;
import com.sumui.service.service.LoginService;
import com.sumui.service.service.SysUserService;
import com.sumui.service.service.system.SysOperLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description
 * @Author @Sunl
 * @Date 2023/12/6 20:22
 */
@Service
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
        // 获取账号信息

        return "ashdfgakjshdgfajhsdgfkjhasdgfghasdfhasdgfkjiuretlwerjntbnb";
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
