package com.sumui.common.annotation;

import com.sumui.common.constants.OperateTypeEnum;

import java.lang.annotation.*;

/**
 * 操作日志
 * @Description
 * @Author @Sunl
 * @Date 2023/12/6 19:49
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {

    /**
     * 模块
     */
    String title() default "";

    /**
     * 操作类型
     */
    OperateTypeEnum businessType() default OperateTypeEnum.OTHER;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;

    /**
     * 排除指定的请求参数
     */
    String[] excludeParamNames() default {};
    
}
