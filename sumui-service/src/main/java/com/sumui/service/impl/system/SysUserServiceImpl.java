package com.sumui.service.impl.system;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sumui.common.constants.StatusEnum;
import com.sumui.common.exception.ApiException;
import com.sumui.common.exception.BizException;
import com.sumui.common.model.security.RegisterUserInfo;
import com.sumui.common.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sumui.common.utils.uuid.IDUtils;
import com.sumui.dao.mapper.system.SysUserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

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
    public Boolean registerUserInfo(RegisterUserInfo registerUserInfo) throws Exception {
        // 校验用户信息
        this.verifyUserInfo(registerUserInfo);
        // 生成用户信息
        SysUser saveUser = this.convertToPojo(registerUserInfo);
        log.error("saveUser:{}",saveUser);

        return this.save(saveUser);
    }

    /**
     * 转换成数据库实体类
     * @param registerUserInfo
     * @return
     */
    private SysUser convertToPojo(RegisterUserInfo registerUserInfo) throws Exception {
        SysUser user = new SysUser();
        user.setId(IDUtils.nextId());
        // todo 后面放开单独设置昵称
        user.setNickname(registerUserInfo.getNickname());
        user.setUsername(registerUserInfo.getUsername());

        HashMap<String, String> rsaGenerateKeyPair = SaSecureUtil.rsaGenerateKeyPair();
        String publicKey = rsaGenerateKeyPair.get("public");
        user.setPassword(SaSecureUtil.rsaEncryptByPublic(publicKey, registerUserInfo.getPassword()));

        user.setSalt(rsaGenerateKeyPair.get("private"));
        user.setMobile(registerUserInfo.getMobile());
        user.setEmail(registerUserInfo.getEmail());
        user.setAvatar(registerUserInfo.getAvatar());
        user.setSex(registerUserInfo.getSex());
        user.setStatus(0);
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

    @Override
    public void wxUserInfo(String accessToken, String openId) {
//        String url = wxUrl + "&appid=" + accessToken + "&secret=" + openId + "&js_code=" + code;
//        try (HttpResponse response = HttpUtil.createGet(url).execute()) {
//            String resp = response.body();
//            log.info("resp:{}", resp);
//            JSONObject parsedObj = JSONUtil.parseObj(resp);
//            if (ObjectUtil.isEmpty(parsedObj)) {
//                return ReqResult.fail(StatusEnum.FAIL, "获取用户信息失败");
//            }
//        }
    }

    /**
     * 更新用户头像
     *
     * @param userId   用户ID
     * @param filePath 文件路径
     * @param fileName 文件名
     */
    @Override
    public void updateUserAvatar(String userId, String filePath, String fileName) {
        if (StrUtil.isBlank(userId)){
            throw new BizException(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        if (StrUtil.isBlank(filePath) || StrUtil.isBlank(fileName)){
            throw new BizException(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        this.lambdaUpdate()
                .set(SysUser::getAvatar, filePath + "/" + fileName)
                .eq(SysUser::getId, userId)
                .update();
    }

    @Override
    public Boolean updateUserNickName(SysUser user) {
        if (StrUtil.isBlank(user.getId())){
            throw new NotLoginException(NotLoginException.DEFAULT_MESSAGE, "401", "");
        }
        if (StrUtil.isBlank(user.getNickname()) && StrUtil.isBlank(user.getUsername())){
            throw new BizException(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        return this.updateById(user);
    }
}
