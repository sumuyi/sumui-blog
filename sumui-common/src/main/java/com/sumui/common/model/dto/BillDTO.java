package com.sumui.common.model.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class BillDTO implements Serializable {
    private String id;
    private String userId;
    private String bookId;
    private String userName;
    private BigDecimal amount;      // 金额
    private String categoryId;        // 账单类别
    private String description;     // 描述
    private String billDate;         // 账单日期
    private Integer type;          // 类型：1-支出，2-收入
    private String remark;         // 备注
}
