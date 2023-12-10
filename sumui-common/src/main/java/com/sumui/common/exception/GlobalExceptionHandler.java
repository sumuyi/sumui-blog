package com.sumui.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
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
     * 基础异常处理
     */
    @ExceptionHandler(ApiException.class)
    public ReqResult<Object> exception(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        return buildBody(ex, request.getRequestURI());
    }

    /**
     * 处理因为数据库唯一索引导致的异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ReqResult<Object> duplicateKeyException(DuplicateKeyException ex, HttpServletRequest request, HttpServletResponse response) {
        String errorMessage = StrUtil.blankToDefault(Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse(null), ex.getMessage());
        if (StrUtil.isBlank(errorMessage)) {
            return buildBody(ex, request.getRequestURI());
        }
        // 提取重复字段
        Matcher matcher = Pattern.compile(REGX_DUPLICATE_KEY).matcher(errorMessage);
        if (matcher.find()) {
            String duplicateField = matcher.group(2);
            String duplicateFieldValue = matcher.group(1);
            if (StrUtil.isNotBlank(duplicateField)) {
                return buildBody(StrUtil.format("[{}]({})数据重复", duplicateField, duplicateFieldValue), ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode(), request.getRequestURI());
            }
        }
        return buildBody(ex, request.getRequestURI());
    }

    /**
     * 处理请求参数缺失异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ReqResult<Object> missingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response) {
        String errorMessage = StrUtil.format("缺失请求参数[{}]" , ex.getParameterName());
        return buildBody(errorMessage, ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode(), request.getRequestURI());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ReqResult<Object> dataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request, HttpServletResponse response) {
        String errorMessage = StrUtil.blankToDefault(Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse(null), ex.getMessage());
        if (StrUtil.isBlank(errorMessage)) {
            return buildBody(ex, request.getRequestURI());
        }
        String passwordRegex = "'([^']+)'";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(errorMessage);
        if (matcher.find()) {
            String errField = matcher.group(1);
            return buildBody(StrUtil.format("[{}]长度超过限制" , errField), ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode(), request.getRequestURI());
        }

        return buildBody(ex, request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        String message = Optional.ofNullable(e.getBindingResult().getFieldError()).map(FieldError::getDefaultMessage).orElse(e.getMessage());
        return buildBody(message, null, request.getRequestURI());
    }

    @ExceptionHandler(NotLoginException.class)
    public ReqResult<Object> notLoginException(NotLoginException ex, HttpServletRequest request, HttpServletResponse response) {
        String message = null;
        switch (ex.getType()) {
            case NotLoginException.NOT_TOKEN:
                message = NotLoginException.NOT_TOKEN_MESSAGE;
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
                message = NotLoginException.DEFAULT_MESSAGE;
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
