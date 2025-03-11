package com.sumui.common.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 账本DTO
 */
@Data
public class BooksDTO {

    private String id;

    /**
     * 账本名称
     */
    private String name;

    /**
     * 账本类型：personal个人，family家庭，travel旅行，etc其他
     */
    private String type;

    /**
     * 账本描述
     */
    private String description;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    /**
     * 预算金额
     */
    private BigDecimal totalIncome;

    /**
     * 预算金额
     */
    private BigDecimal totalExpense;

    /**
     * 结余金额
     */
    private BigDecimal balanceAmount;

    /**
     * 账本图标
     */
    private String icon;

    /**
     * 账本封面
     */
    private String coverImage;

    /**
     * 账单数量
     */
    private Integer billCount;

    /**
     * 是否为当前选中账本
     */
    private Boolean isDefault;

    /**
     * 成员列表
     */
    private String[] memberIds;
}
