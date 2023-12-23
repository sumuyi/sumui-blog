package com.sumui.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sumui.common.model.system.SysUser;
import com.sumui.common.utils.uuid.IDUtils;
import com.sumui.common.utils.uuid.SnowFlakeUtil;
import com.sumui.dao.mapper.SysUserMapper;
import com.sumui.service.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author @Sunl
 * @Date 2023/12/10 15:48
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public void getUUID() {
        System.err.println("SysUserServiceImpl------" + IDUtils.nextId());
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    @Override
    public SysUser getUserInfoByUserName(String username) {
        if (StrUtil.isBlank(username)){
            return new SysUser();
        }
        return this.lambdaQuery().eq(SysUser::getUsername,username).one();
    }
}
