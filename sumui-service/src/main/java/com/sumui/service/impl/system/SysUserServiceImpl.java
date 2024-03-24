package com.sumui.service.impl.system;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sumui.common.constants.StatusEnum;
import com.sumui.common.exception.ApiException;
import com.sumui.common.model.security.RegisterUserInfo;
import com.sumui.common.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sumui.common.utils.uuid.IDUtils;
import com.sumui.dao.mapper.system.SysUserMapper;
import com.sumui.service.service.system.SysUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author sumui
 * @since 2023-12-23 11:16:53
 */
@Service
@Log4j2
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
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

    /**
     * 注册用户信息
     *
     * @param registerUserInfo
     * @return
     */
    @Override
    public Boolean registerUserInfo(RegisterUserInfo registerUserInfo) {
        // 校验用户信息
        this.verifyUserInfo(registerUserInfo);
        // 生成用户信息
        SysUser saveUser = this.convertToPojo(registerUserInfo);
        log.error("saveUser:{}",saveUser);
        return Boolean.TRUE;
    }

    /**
     * 转换成数据库实体类
     * @param registerUserInfo
     * @return
     */
    private SysUser convertToPojo(RegisterUserInfo registerUserInfo) {
        SysUser user = new SysUser();
        user.setId(IDUtils.nextId());
        // todo 后面放开单独设置昵称
//        user.setNickname(registerUserInfo.getUsername());
        user.setUsername(registerUserInfo.getUsername());
        user.setPassword(registerUserInfo.getPassword());
//        user.setSalt();
        user.setMobile(registerUserInfo.getMobile());
        user.setEmail(registerUserInfo.getEmail());
        user.setAvatar(registerUserInfo.getAvatar());
        user.setSex(registerUserInfo.getSex());
        user.setStatus(1);
        user.setIsSuper(0);
//        user.setDeptId();
//        user.setLoginIp();
//        user.setLoginTime();
//        user.setLastActiveTime();
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setCreateBy("1");
        user.setUpdateBy("1");
        return user;
    }

    private void verifyUserInfo(RegisterUserInfo registerUserInfo) {
        if (CharSequenceUtil.isBlank(registerUserInfo.getUsername())){
            throw new ApiException("请输入用户名");
        }
        if (CharSequenceUtil.isBlank(registerUserInfo.getPassword())){
            throw new ApiException("请输入用户密码");
        }
        if (CharSequenceUtil.isBlank(registerUserInfo.getConfirmPwd())){
            throw new ApiException("请输入确认密码");
        }
        if (ObjectUtil.notEqual(registerUserInfo.getPassword(),registerUserInfo.getConfirmPwd())){
            throw new ApiException("两次输入的密码不一致");
        }
        if (CharSequenceUtil.isBlank(registerUserInfo.getEmail())){
            throw new ApiException("请输入用户邮箱");
        }
        if (CharSequenceUtil.isBlank(registerUserInfo.getMobile())){
            throw new ApiException("请输入用户手机号");
        }
        // todo 校验用户邮箱与手机号规则
        // 校验用户名是否重复
        if (this.userIsExist(registerUserInfo.getUsername())){
            throw new ApiException("用户名已存在，请重新输入");
        }
    }

    /**
     * 判断用户名是否存在
     * @param userName
     * @return
     */
    @Override
    public Boolean userIsExist(String userName) {
        LambdaQueryWrapper<SysUser> query = Wrappers.lambdaQuery();
        query.eq(SysUser::getUsername, userName);
        return baseMapper.selectCount(query) >= 1;
    }
}
