package com.sumui.common.model;

import com.sumui.common.constants.ErrorCodeEnum;
import com.sumui.common.constants.StatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 全局统一封装--返回实体类
 * @Author @Sunl
 * @Date 2023/12/5 19:10
 */
@Data
public class ReqResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Status status;
    private T result;
    private Integer code;
    private String message;


    public ReqResult() {
    }

    public ReqResult(Status status) {
        this.status = status;
    }

    public ReqResult(T t) {
        this.status = Status.newStatus(StatusEnum.SUCCESS);
        this.result = t;
    }

    public ReqResult(StatusEnum statusEnum, T t) {
        this.status = Status.newStatus(statusEnum);
        this.result = t;
    }

    public static <T> ReqResult<T> ok() {
        return ReqResult.ok(StatusEnum.SUCCESS);
    }

    public static <T> ReqResult<T> ok(StatusEnum statusEnum) {
        ReqResult<T> result = new ReqResult<>();
        result.setCode(statusEnum.getCode());
        result.setMessage(statusEnum.getMsg());
        return result;
    }

    public static <T> ReqResult<T> ok(T t) {
        ReqResult<T> result = new ReqResult<>();
        result.setCode(StatusEnum.SUCCESS.getCode());
        result.setMessage(StatusEnum.SUCCESS.getMsg());
        result.setResult(t);
        return result;
    }

    public static <T> ReqResult<T> fail(T t) {
        return new ReqResult<T>(StatusEnum.FAIL,t);
    }

    public static <T> ReqResult<T> fail(StatusEnum StatusEnum) {
        ReqResult<T> result = new ReqResult<>();
        result.setCode(StatusEnum.getCode());
        result.setMessage(StatusEnum.getMsg());
        return result;
    }
    
    public static <T> ReqResult<T> fail(Integer code, String message) {
        ReqResult<T> result = new ReqResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
