package com.sumui.common.utils.uuid;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

public class IDUtils {
    public static Long workId = 0L;
    private static SnowFlakeUtil idWorker;

    public static SnowFlakeUtil getIdWorkerInstance() {
        if (idWorker == null) {
            idWorker = new SnowFlakeUtil(workId);
        }
        return idWorker;
    }

    public static String nextId() {
        return String.valueOf(IDUtils.getIdWorkerInstance().nextId());
    }
}
