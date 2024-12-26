package com.sumui.common.model.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录用户信息返回实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVO implements Serializable {
    private String userId;
    private String saToken;
    private Long expireTime;
    private String userName;
    private String nickname;
    private String avatar;
    private String email;
    private String mobile;
}
