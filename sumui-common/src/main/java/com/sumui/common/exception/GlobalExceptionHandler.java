package com.sumui.common.exception;


import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.core.util.StrUtil;
import com.sumui.common.constants.ErrorCodeEnum;
import com.sumui.common.model.ReqResult;
import com.sumui.common.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 全局异常处理
 * @author Sunl
 * @since 2023/12/6
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    final String REGX_DUPLICATE_KEY = "Duplicate entry '(.*)' for key '(.*)'";

    /**
     * Sa-Token 权限认证异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public ReqResult<?> handleNotPermissionException(NotPermissionException e) {
        log.error("没有权限，{}", e.getMessage());
        return ReqResult.fail("没有权限访问：" + e.getMessage());
    }

    /**
     * Sa-Token 角色认证异常
     */
    @ExceptionHandler(NotRoleException.class)
    public ReqResult<?> handleNotRoleException(NotRoleException e) {
        log.error("没有角色，{}", e.getMessage());
        return ReqResult.fail("没有角色权限：" + e.getMessage());
    }

    /**
     * Sa-Token 未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public ReqResult<?> handleNotLoginException(NotLoginException ex, HttpServletRequest request, HttpServletResponse response) {
        log.error("未登录，{}", ex.getMessage());
        String message;
        switch (ex.getType()) {
            case NotLoginException.NOT_TOKEN:
                message = "未提供token";
                break;
            case NotLoginException.INVALID_TOKEN:
                message = NotLoginException.INVALID_TOKEN_MESSAGE;
                break;
            case NotLoginException.TOKEN_TIMEOUT:
                message = NotLoginException.TOKEN_TIMEOUT_MESSAGE;
                break;
            case NotLoginException.BE_REPLACED:
                message = NotLoginException.BE_REPLACED_MESSAGE;
                break;
            case NotLoginException.KICK_OUT:
                message = NotLoginException.KICK_OUT_MESSAGE;
                break;
            default:
                message = "当前会话未登录";
        }
        return buildBody(message, ErrorCodeEnum.UNAUTHORIZED.getCode(), request.getRequestURI());
    }
    
    private static ReqResult<Object> buildBody(Exception exception, String uri) {
        log.error(exception.getMessage(), exception);
        String errMessage = StrUtil.blankToDefault(
                Optional.ofNullable(exception.getCause()).map(Throwable::getMessage).orElse(null),
                exception.getMessage()
        );
        return buildBody(errMessage, null, uri);
    }

    private static ReqResult<Object> buildBody(String message, Integer resultCode, String uri) {
        if (StrUtil.isBlank(message)) {
            message = ErrorCodeEnum.INTERNAL_SERVER_ERROR.getDescription();
        }
        if (resultCode == null) {
            resultCode = ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode();
        }
        ReqResult<Object> reqResult = new ReqResult<>();
        reqResult.setResult(uri);
        reqResult.setStatus(Status.newStatus(resultCode,message));
        return reqResult;
    }

}
