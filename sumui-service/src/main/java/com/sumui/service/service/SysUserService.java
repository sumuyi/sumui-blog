package com.sumui.service.service;

import com.sumui.common.model.system.SysUser;

/**
 * @Description
 * @Author @Sunl
 * @Date 2023/12/10 15:47
 */
public interface SysUserService {
    void getUUID();

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    SysUser getUserInfoByUserName(String username);
}
