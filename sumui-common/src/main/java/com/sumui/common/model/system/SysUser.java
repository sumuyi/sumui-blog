package com.sumui.common.model.system;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @description: 用户表
 * @author: 周振林
 * @date: 2022/4/9
 *
 * @Copyright: 博客：http://www.zhouzhenlin.com - 沉淀、分享、成长、让自己和他人都有所收获
 **/

@Data
@Accessors(chain = true)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String tel;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否是超级管理员
     */
    private Integer isSuper;

    @TableField(fill = FieldFill.INSERT)
    private Integer deptId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Integer createBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateBy;

    /**
     * 删除标记 1:已删除,0:正常
     */
    @TableLogic
    private Integer delFlag;

}
