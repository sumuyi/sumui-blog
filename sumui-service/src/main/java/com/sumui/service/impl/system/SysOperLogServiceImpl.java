package com.sumui.service.impl.system;

import com.sumui.common.model.system.SysOperLog;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sumui.common.utils.uuid.IDUtils;
import com.sumui.dao.mapper.system.SysOperLogMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author sumui
 * @since 2023-12-15 04:19:00
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {
    /**
     *
     */
    @Override
    public void getUUID() {
        System.err.println("SysOperLogServiceImpl------" + IDUtils.nextId());
    }
}
