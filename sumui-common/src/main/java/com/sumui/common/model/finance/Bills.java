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
 * 账单表
 * @TableName bills
 */
@TableName(value ="bills")
@Data
public class Bills implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 账本ID
     */
    private Long bookId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 类型：1-支出，2-收入
     */
    private Integer type;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 账单日期
     */
    private Date billDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 消费地点
     */
    private String location;

    /**
     * 票据图片，JSON数组存储多个图片URL
     */
    private String images;

    /**
     * 创建人ID
     */
    private Long createdBy;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}