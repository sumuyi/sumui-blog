package com.sumui.common.model.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 账本表
 * @TableName books
 */
@TableName(value ="books")
@Data
public class Books implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private String id;

    private String userId;

    /**
     *
     */
    private String name;

    /**
     * 账本类型：personal个人，family家庭，travel旅行，etc其他
     */
    private String type;

    /**
     * 账本预算金额
     */
    private BigDecimal budgetAmount;

    /**
     *
     */
    private String description;

    /**
     * 账本封面图片
     */
    private String coverImage;

    /**
     * 账本图标
     */
    private String icon;

    /**
     * 状态：0正常，1停用，2归档
     */
    private Integer status;

    /**
     * 是否为当前选中账本
     */
    @TableField(exist = false)
    private Boolean isSelected;

    /**
     *
     */
    private String createdBy;
    private Date createdAt;

    /**
     *
     */
    private String updatedBy;
    private Date updatedAt;

    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
