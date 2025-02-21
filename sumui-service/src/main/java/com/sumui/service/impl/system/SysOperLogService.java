package com.sumui.service.impl.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sumui.common.model.system.SysOperLog;

/**
 * <p>
 * 操作日志 服务类
 * </p>
 *
 * @author sumui
 * @since 2023-12-15 04:19:00
 */
public interface SysOperLogService extends IService<SysOperLog> {

    void getUUID();
}
