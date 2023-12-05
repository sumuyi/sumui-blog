package com.sumui.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 异常状态码
 * @Author @Sunl
 * @Date 2023/12/5 19:10
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

    SUCCESS(200, "success"),
    ERROR(500, "error"),
    WARN(600, "warn"),

    NOT_LOGIN(401, "未登录"),
    NOT_PERMISSION(403, "未授权"),

    ;


    private final int code;
    private final String message;

}
