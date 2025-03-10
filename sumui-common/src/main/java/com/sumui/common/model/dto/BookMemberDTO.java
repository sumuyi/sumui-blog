package com.sumui.common.model.dto;

import lombok.Data;

/**
 * 账本成员DTO
 */
@Data
public class BookMemberDTO {

    private String id;

    /**
     * 账本ID
     */
    private String bookId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 权限：read只读，write读写
     */
    private String permission;

    /**
     * 在账本中的昵称
     */
    private String nickname;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 头像
     */
    private String avatar;
}
