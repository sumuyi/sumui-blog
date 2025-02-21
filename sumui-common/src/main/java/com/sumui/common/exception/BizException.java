package com.sumui.common.exception;

import com.sumui.common.constants.StatusEnum;
import lombok.Getter;

@Getter
public class BizException extends RuntimeException {
    
    private final Integer code;
    private final String message;
    
    public BizException(StatusEnum resultCodeEnum) {
        super(resultCodeEnum.getMsg());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMsg();
    }
    
    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}