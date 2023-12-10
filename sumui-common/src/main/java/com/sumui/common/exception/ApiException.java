package com.sumui.common.exception;

import com.sumui.common.constants.ErrorCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @Description
 * @Author @Sunl
 * @Date 2023/12/10 16:00
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Data
public class ApiException extends RuntimeException{
    private final int code;

    private final String description;

    public ApiException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public ApiException(ErrorCodeEnum errorCode) {
        super(errorCode.getDescription());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public ApiException(ErrorCodeEnum errorCode, String description) {
        super(errorCode.getDescription());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public ApiException(String description) {
        super(description);
        this.code = ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode();
        this.description = description;
    }

}
