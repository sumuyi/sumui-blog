package com.sumui.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 异常状态码
 * @Author @Sunl
 * @Date 2023/12/5 19:10
 */
@Getter
public enum ErrorCodeEnum {

    OK(200, "请求成功"),
    CREATED(201, "资源创建成功"),
    NO_CONTENT(204, "无内容返回"),
    BAD_REQUEST(400, "错误的请求"),
    UNAUTHORIZED(401, "未授权的访问"),
    FORBIDDEN(403, "禁止访问"),
    TIMEOUT(408, "请求超时"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用");

    private final int code;
    private final String description;

    ErrorCodeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
