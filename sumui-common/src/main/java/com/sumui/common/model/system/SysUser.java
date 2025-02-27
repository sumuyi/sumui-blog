package com.sumui.common.model.system;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author sumui
 * @since 2023-12-23 14:19:00
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value = "SysUser对象", description = "用户信息")
public class SysUser implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 昵称
     */
    private String openId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 加密盐
     */
    private String salt;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 账号状态 0：停用 1:正常
     */
    private Integer status;

    /**
     * 是否是超级管理员 1：是 0:否
     */
    private Integer isSuper;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 家庭账本ID
     */
    private Long familyBookId;

    /**
     * 登录IP
     */
    private String loginIp;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTime;

    /**
     * 最后一次活跃时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastActiveTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志
     */
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 格式化用户名: 获取昵称，没有返回用户名
     * @return 昵称或用户名
     */
    public String formatUserName() {
        return StrUtil.isNotBlank(nickname) ? nickname : username;
    }
}