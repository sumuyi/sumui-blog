package com.sumui.common.utils;

import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 工具类 - ip/浏览器/操作系统/浏览器版本
 *
 * @author ao.nie
 * @date 2022/4/14
 */
public class HttpUtils {


    private HttpUtils() {
    }


    /**
     * 请求url
     *
     * @return {@link String}
     */
    public static String getRequestUrl() {
        return StrUtil.sub(SpringMVCUtil.getRequest().getRequestURI(), 0, 255);
    }

    /**
     * 获取浏览器名字
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String getBrowserName(HttpServletRequest request) {
        String uaStr = request.getHeader("User-Agent");
        UserAgent ua = UserAgentUtil.parse(uaStr);
        return ua.getBrowser().toString();
    }

    /**
     * 获取浏览器版本
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String getBrowserVersion(HttpServletRequest request) {
        String uaStr = request.getHeader("User-Agent");
        UserAgent ua = UserAgentUtil.parse(uaStr);
        return ua.getVersion();
    }

    /**
     * 获取操作系统名称
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String getOsName(HttpServletRequest request) {
        String uaStr = request.getHeader("User-Agent");
        UserAgent ua = UserAgentUtil.parse(uaStr);
        return ua.getOs().toString();
    }


    /**
     * 得到ip addr
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String getIpAddr(HttpServletRequest request) {
        return IpUtil.getClientIp(request);
    }
}
