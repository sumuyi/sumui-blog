package com.sumui.config.satoken;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: Sa-Token自定义权限验证接口扩展
 * @author: Sunl
 * @date: 2023-12-21
 **/
@Component
public class StpInterfaceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // todo 通过用户ID 获取到角色ID
        

        // todo 通过角色ID 获取到菜单ID


        // todo 通过菜单ID 查询所有权限

        return new ArrayList<>();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {

        // todo 通过用户ID 查询角色ID


        // todo 通过角色ID 查询角色名称

//        StpUtil.getSession().set(CacheConstant.ROLE_DETAIL,roleIds);
        return new ArrayList<>();
    }
}
