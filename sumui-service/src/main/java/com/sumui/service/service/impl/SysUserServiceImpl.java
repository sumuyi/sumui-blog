package com.sumui.service.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
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
public class SysUserServiceImpl implements SysUserService {

    @Override
    public void getUUID() {
        System.err.println("SysUserServiceImpl------" + IDUtils.nextId());
    }
}
