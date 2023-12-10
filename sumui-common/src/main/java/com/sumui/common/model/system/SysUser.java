package com.sumui.common.model.system;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zhaoeryu
 * @since 2023/5/31
 */
@Data
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    @ExcelProperty("ID")
    @TableId
    private String id;
    /** 登录名 */
    @ExcelProperty("登录名")
    @NotBlank(message = "登录名不能为空")
    private String username;
    /** 用户昵称 */
    @ExcelProperty("用户昵称")
    @NotBlank(message = "用户昵称不能为空")
    private String nickname;
    /** 用户头像 */
    @ExcelProperty(value = "头像")
    private String avatar;
    /** 手机号 */
    @ExcelProperty("手机号")
    private String mobile;
    /** 邮箱 */
    @ExcelProperty("邮箱")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String email;
    /** 密码 */
    @ExcelIgnore
    @JsonIgnore // 密码不返回给前端
    private String password;
    /** 性别 */
    @ExcelProperty(value = "性别")
    private Integer sex;
    /** 是否管理员 */
    @ExcelProperty(value = "是否管理员")
    private Integer admin;
    /** 账号状态 */
    @ExcelProperty(value = "账号状态")
    private Integer status;
    /** 登录IP */
    @ExcelProperty("登录IP")
    private String loginIp;
    /** 登录时间 */
    @ExcelProperty("登录时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;
    /** 最后一次活跃时间 */
    @ExcelProperty("最后一次活跃时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastActiveTime;
    /** 密码重置时间 */
    @ExcelProperty("密码重置时间")
    private LocalDateTime passwordResetTime;

}
