package com.sumui.common.exception;

import com.sumui.common.constants.StatusEnum;
import com.sumui.common.model.Status;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author Sunl
 * @date 2023-12-22
 */
public class UserException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    @Getter
    private Status status;

    public UserException(Status status) {
        this.status = status;
    }

    public UserException(int code, String msg) {
        this.status = Status.newStatus(code, msg);
    }

    public UserException(StatusEnum statusEnum) {
        this.status = Status.newStatus(statusEnum.getCode(), statusEnum.getMsg());
    }

    public UserException(StatusEnum statusEnum, Object... args) {
        this.status = Status.newStatus(statusEnum, args);
    }

}
