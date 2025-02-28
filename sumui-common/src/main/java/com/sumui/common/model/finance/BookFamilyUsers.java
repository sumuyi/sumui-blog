package com.sumui.common.model.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 家庭账本关联人员
 * @TableName book_family_users
 */
@TableName(value ="book_family_users")
@Data
public class BookFamilyUsers implements Serializable {
    /**
     * 家庭ID
     */
    @TableId
    private String id;

    /**
     * 家庭账本ID
     */
    private String bookId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 创建人
     */
    private String createBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
