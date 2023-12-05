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
    @ApiModelProperty(value = "返回结果说明", required = true)
    private Status status;

    @ApiModelProperty(value = "返回的实体结果", required = true)
    private T result;


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

    public static <T> ReqResult<T> ok(T t) {
        return new ReqResult<T>(t);
    }

    public static <T> ReqResult<T> fail(T t) {
        return new ReqResult<T>(StatusEnum.FAIL,t);
    }

    public static <T> ReqResult<T> fail(StatusEnum status, Object... args) {
        return new ReqResult<>(Status.newStatus(status, args));
    }

    public static <T> ReqResult<T> fail(Status status) {
        return new ReqResult<>(status);
    }

}
