package com.sumui.service.impl.system;

import com.sumui.common.model.security.RegisterUserInfo;
import com.sumui.common.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author sumui
 * @since 2023-12-23 11:16:53
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    SysUser getUserInfoByUserName(String username);

    /**
     * 注册用户信息
     * @param registerUserInfo
     * @return
     */
    Boolean registerUserInfo(RegisterUserInfo registerUserInfo) throws Exception;

    Boolean userIsExist(String userName);

    void wxUserInfo(String accessToken, String openId);

    /**
     * 更新用户头像
     * @param userId    用户ID
     * @param filePath  文件路径
     * @param fileName  文件名
     */
    void updateUserAvatar(String userId, String filePath, String fileName);

    Boolean updateUserNickName(SysUser user);
}
