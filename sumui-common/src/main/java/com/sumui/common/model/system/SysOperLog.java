package com.sumui.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author sumui
 * @since 2023-12-15 04:19:00
 */
@Data
@Accessors(chain = true)
@TableName("sys_oper_log")
@ApiModel(value = "SysOperLog对象", description = "操作日志")
public class SysOperLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty("操作模块")
    @TableField("title")
    private String title;

    @ApiModelProperty("业务类型")
    @TableField("business_type")
    private Integer businessType;

    @ApiModelProperty("执行方法")
    @TableField("method")
    private String method;

    @ApiModelProperty("Http请求方式")
    @TableField("req_method")
    private String reqMethod;

    @ApiModelProperty("操作人")
    @TableField("oper_user_id")
    private String operUserId;

    @ApiModelProperty("操作人名称")
    @TableField("oper_user_name")
    private String operUserName;

    @ApiModelProperty("请求URL")
    @TableField("req_url")
    private String reqUrl;

    @ApiModelProperty("请求IP")
    @TableField("req_ip")
    private String reqIp;

    @ApiModelProperty("请求地域")
    @TableField("req_region")
    private String reqRegion;

    @ApiModelProperty("请求参数")
    @TableField("req_params")
    private String reqParams;

    @ApiModelProperty("响应结果")
    @TableField("resp_result")
    private String respResult;

    @ApiModelProperty("浏览器")
    @TableField("browser")
    private String browser;

    @ApiModelProperty("操作系统")
    @TableField("os")
    private String os;

    @ApiModelProperty("操作状态")
    @TableField("status")
    private Boolean status;

    @ApiModelProperty("错误消息")
    @TableField("error_msg")
    private String errorMsg;

    @ApiModelProperty("异常堆栈")
    @TableField("error_stack")
    private String errorStack;

    @ApiModelProperty("执行时长")
    @TableField("exec_time")
    private Integer execTime;

    @ApiModelProperty("创建人")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改人")
    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("删除标志")
    @TableField("del_flag")
    private Boolean delFlag;


}
