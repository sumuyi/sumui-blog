package com.sumui.common.model.dto;

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
     * 预算金额
     */
    private BigDecimal budgetAmount;

    /**
     * 账本图标
     */
    private String icon;

    /**
     * 账本封面
     */
    private String coverImage;

    /**
     * 成员列表
     */
    private String[] memberIds;
}
