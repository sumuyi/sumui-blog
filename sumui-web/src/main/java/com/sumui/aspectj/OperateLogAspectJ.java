package com.sumui.aspectj;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import com.sumui.common.annotation.OperateLog;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Description aop切面日志
 * @Author @Sunl
 * @Date 2023/12/12 19:30
 */
@Aspect
@Component
@Log4j2
public class OperateLogAspectJ {

    /**
     * 切点
     * 注意：这里的路径最好写全路径 不写全有时会有错误
     *
     * 参数说明：
     * 使用@annotation时，所修饰的方法或是接口都会被拦截。
     * 使用execution时，指定你要切入的一个地方。
     */
    @Pointcut("@annotation(com.sumui.common.annotation.OperateLog)")
    public void operatePointCut(){}

    /**
     * 处理请求前执行
     */
    @Around(value = "operatePointCut()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        // 记录开始时长
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = joinPoint.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        // 记录日志
        recordLog(joinPoint, time);
        return result;
    }

    /**
     * 记录操作日志
     * @param joinPoint
     * @param time
     */
    private void recordLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperateLog logAnnotation = method.getAnnotation(OperateLog.class);
        log.info("===============================日志开始===============================");
        log.info("日志模块：{}", logAnnotation.title());
        log.info("日志操作类型：{}", logAnnotation.businessType());
        log.info("请求方法：{}", joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()");
        log.info("请求参数：{}", JSON.toJSONString(joinPoint.getArgs()));
        log.info("请求时长：{} ms", time);
        log.info("请求IP：{}", getRequestIp());
        log.info("===============================日志结束===============================");
    }

    /**
     * 获取请求IP地址
     */
    private String getRequestIp() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        assert request != null;
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
