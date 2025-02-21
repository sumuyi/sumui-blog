package com.sumui.common.model.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
    private Long id;

    /**
     * 
     */
    private String name;

    /**
     * 账本类型：daily-日常，travel-旅行等
     */
    private String type;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private String coverImage;

    /**
     * 账本状态：1-正常，0-已删除
     */
    private Integer status;

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