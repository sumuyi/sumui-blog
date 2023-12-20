package com.sumui.common.utils.uuid;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SnowFlakeUtil {

    //下面两个每个5位，加起来就是10位的工作机器id
    private static volatile long workerId;    //工作id
    private static volatile long datacenterId;   //数据id
    //12位的序列号
    private static volatile long sequence;

    public SnowFlakeUtil(long workerId, long datacenterId, long sequence) {
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        System.out.printf("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, workerid %d",
                timestampLeftShift, datacenterIdBits, workerIdBits, sequenceBits, workerId);

        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
    }

    //初始时间戳
    private static final long twepoch = 1288834974657L;

    /** 机器ID所占位数，长度为5位 */
    private static final long workerIdBits = 5L;

    /** 数据标识ID所占位数，长度位5 */
    private static final long datacenterIdBits = 5L;
    /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    /** 支持的最大数据标识id，结果是31 */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    /** 序列在id中占的位数 */
    private static final long sequenceBits = 12L;
    /** 序列号最大值 */
    private static final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** 工作机器ID向左移12位 */
    private static final long workerIdShift = sequenceBits;
    /** 数据id需要左移位数 12+5=17位 */
    private static final long datacenterIdShift = sequenceBits + workerIdBits;
    /** 时间戳需要左移位数 12+5+5=22位 */
    private static final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /** 上次时间戳，初始值为负数 */
    private static volatile long lastTimestamp = -1L;

    public long getWorkerId() {
        return workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *  如果一个线程反复获取Synchronized锁，那么synchronized锁将变成偏向锁。
     * @return 生成的ID
     */
    public static synchronized long nextId() {
        log.error(workerId + "-" + datacenterId + "-" + sequence);

        // 获取当前时间的时间戳，单位（毫秒）
        long timestamp = timeGen();

        //获取当前时间戳如果小于上次时间戳，则表示时间戳获取出现异常
        if (timestamp < lastTimestamp) {
            System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }

        //获取当前时间戳如果等于上次时间戳（同一毫秒内），则在序列号加一；否则序列号赋值为0，从0开始。
        if (lastTimestamp == timestamp) {
            /* 逻辑：意思是说一个毫秒内最多只能有4096个数字，无论你传递多少进来，
                    这个位运算保证始终就是在4096这个范围内，避免你自己传递个sequence超过了4096这个范围 */
            // sequence：毫秒内序列(0~4095);  sequenceMask: 序列号最大值;
            sequence = (sequence + 1) & sequenceMask;

            /* 逻辑：当某一毫秒的时间，产生的id数 超过4095，系统会进入等待，直到下一毫秒，系统继续产生ID */
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        // 将上次时间戳值刷新（逻辑：记录一下最近一次生成id的时间戳，单位是毫秒）
        lastTimestamp = timestamp;

        /**
         * 返回结果：
         * (timestamp - twepoch) << timestampLeftShift) 表示将时间戳减去初始时间戳，再左移相应位数
         * (datacenterId << datacenterIdShift) 表示将数据id左移相应位数
         * (workerId << workerIdShift) 表示将工作id左移相应位数
         * | 是按位或运算符，例如：x | y，只有当x，y都为0的时候结果才为0，其它情况结果都为1。
         * 因为个部分只有相应位上的值有意义，其它位上都是0，所以将各部分的值进行 | 运算就能得到最终拼接好的id
         */
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    /**
     * 上次时间戳与当前时间戳进行比较
     * 逻辑：当某一毫秒的时间，产生的id数 超过4095，系统会进入等待，直到下一毫秒，系统继续产生ID
     * @param lastTimestamp 上次时间戳
     * @return 若当前时间戳小于等于上次时间戳（时间回拨了），则返回最新当前时间戳； 否则，返回当前时间戳
     */
    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    //获取系统时间戳
    private static long timeGen() {
        return System.currentTimeMillis();
    }

    //---------------测试---------------
    public static void main(String[] args) {
        SnowFlakeUtil worker = new SnowFlakeUtil(1, 1, 1);
        for (int i = 0; i < 30; i++) {
            System.out.println(worker.nextId());
        }
    }

}