package com.sumui.aspectj;

import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSON;
import com.sumui.common.constants.StatusEnum;
import com.sumui.common.model.system.SysOperLog;
import com.sumui.common.utils.HttpUtils;
import com.sumui.common.utils.IpUtil;
import com.sumui.common.utils.PropertyPreExcludeFilter;
import com.sumui.common.utils.uuid.SnowFlakeUtil;
import com.sumui.service.service.system.SysOperLogService;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import com.sumui.common.annotation.OperateLog;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

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
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password" , "oldPassword" , "newPassword"};

    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("_SUNL_OperationElapsedTime");

    @Resource
    private SysOperLogService sysOperLogService;

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
        TIME_THREADLOCAL.set(beginTime);
        // 执行方法
        Object result = joinPoint.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        // 记录日志
        this.recordLog(joinPoint, time, null, result);
        return result;
    }

    @AfterThrowing(value = "@annotation(operationLogger)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, OperateLog operationLogger, Exception e) throws Exception {
        this.recordLog((ProceedingJoinPoint) joinPoint, System.currentTimeMillis() - TIME_THREADLOCAL.get(), e, null);
    }

    /**
     * 记录操作日志
     * @param joinPoint 切点
     * @param time 执行时间
     * @param result 请求结果
     */
    private void recordLog(ProceedingJoinPoint joinPoint, long time, final Exception exception, Object result) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            OperateLog logAnnotation = method.getAnnotation(OperateLog.class);
            String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";
            HttpServletRequest request = SpringMVCUtil.getRequest();

            log.info("===============================日志开始===============================");
            log.info("日志模块：{}", logAnnotation.title());
            log.info("日志操作类型：{}", logAnnotation.businessType());
            log.info("请求方法：{}", methodName);
            log.info("请求参数：{}", JSON.toJSONString(joinPoint.getArgs()));
            log.info("请求时长：{} ms", time);
            log.info("请求IP：{}", HttpUtils.getIpAddr(request));
            log.info("===============================日志结束===============================");

            SysOperLog operLog = new SysOperLog();
            operLog.setId(String.valueOf(SnowFlakeUtil.nextId()));

            // todo 操作人信息
//            operLog.setOperUserId();
//            operLog.setOperUserName();

            if (exception != null) {
                operLog.setStatus(StatusEnum.FAIL.getCode());
                operLog.setErrorMsg(StrUtil.sub(exception.getMessage(), 0, 255));
                operLog.setErrorStack(ExceptionUtil.stacktraceToString(exception, 8000));
            }

            // 请求信息
            operLog.setReqMethod(request.getMethod());
            operLog.setReqUrl(HttpUtils.getRequestUrl());
            operLog.setReqIp(HttpUtils.getIpAddr(request));
            operLog.setReqRegion(IpUtil.getIpRegion(HttpUtils.getIpAddr(request)));
            operLog.setStatus(StatusEnum.SUCCESS.getCode());

            operLog.setBrowser(HttpUtils.getBrowserName(request));
            operLog.setOs(HttpUtils.getOsName(request));

            // 处理设置注解上的参数
            this.getControllerMethodDescription(joinPoint, logAnnotation, operLog, result);

            // 设置消耗时间
            operLog.setExecTime((int) time);
            operLog.setCreateTime(new Date());
            operLog.setUpdateTime(new Date());

//            operLog.setErrorMsg();
//            operLog.setErrorStack();
//
//            operLog.setCreateBy();
//            operLog.setUpdateBy();

            System.err.println(JSON.toJSONString(operLog));

            sysOperLogService.save(operLog);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, OperateLog log, SysOperLog operLog, Object jsonResult) throws Exception {
        operLog.setMethod(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
        // 设置action动作
        operLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operLog.setTitle(log.title());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operLog, log.excludeParamNames());
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && jsonResult != null) {
            operLog.setRespResult(JSON.toJSONString(jsonResult));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, SysOperLog operLog, String[] excludeParamNames) throws Exception {
        Map<String, String> paramsMap = ServletUtil.getParamMap(SpringMVCUtil.getRequest());
        String requestMethod = operLog.getReqMethod();
        if (CollUtil.isEmpty(paramsMap) && (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operLog.setReqParams(StrUtil.sub(params, 0, 2000));
        } else {
            operLog.setReqParams(StrUtil.sub(JSON.toJSONString(paramsMap, excludePropertyPreFilter(excludeParamNames)), 0, 2000));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null) {
            for (Object o : paramsArray) {
                if (o != null && !isFilterObject(o)) {
                    try {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter(excludeParamNames));
                        params.append(jsonObj).append(" ");
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames) {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtil.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
