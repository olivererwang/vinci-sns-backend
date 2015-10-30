package com.vinci.common.base.monitor.util;

import com.vinci.common.base.monitor.AtomicLongCounter;
import com.vinci.common.base.monitor.QuMonitor;

/**
 * 执行时间性能监控器
 * 
 * @author sunli
 */
public class PerformanceMonitor {
    /**
     * 请求时间小于5ms
     */
    public AtomicLongCounter SLOWREQEUSTLESS5;
    /**
     * 请求时间小于10ms
     */
    public AtomicLongCounter SLOWREQEUSTLESS10;
    /**
     * 请求时间小于20毫秒
     */
    public AtomicLongCounter SLOWREQEUSTLESS20;
    /**
     * 请求时间小于50毫秒
     */
    public AtomicLongCounter SLOWREQEUSTLESS50;
    /**
     * 请求时间小于100毫秒
     */
    public AtomicLongCounter SLOWREQEUSTLESS100;
    /**
     * 请求时间小于1秒
     * @uml.property  name="sLOWREQEUSTLESS1000"
     * @uml.associationEnd  multiplicity="(1 1)"
     */
    public AtomicLongCounter SLOWREQEUSTLESS1000;
    /**
     * 请求时间大于1秒
     */
    public AtomicLongCounter SLOWREQEUSTMORE1000;

    public PerformanceMonitor(String prefix) {
        /**
         * 请求时间小于5ms
         */
        SLOWREQEUSTLESS5 = QuMonitor.createAtomicLongCounter(prefix + ".SLOWREQEUSTLESS5");
        QuMonitor.addPeriodMonitor(SLOWREQEUSTLESS5);
        /**
         * 请求时间小于10ms
         */
        SLOWREQEUSTLESS10 = QuMonitor.createAtomicLongCounter(prefix + ".SLOWREQEUSTLESS10");
        QuMonitor.addPeriodMonitor(SLOWREQEUSTLESS10);
        /**
         * 请求时间小于20毫秒
         */
        SLOWREQEUSTLESS20 = QuMonitor.createAtomicLongCounter(prefix + ".SLOWREQEUSTLESS20");
        QuMonitor.addPeriodMonitor(SLOWREQEUSTLESS20);
        /**
         * 请求时间小于50毫秒
         */
        SLOWREQEUSTLESS50 = QuMonitor.createAtomicLongCounter(prefix + ".SLOWREQEUSTLESS50");
        QuMonitor.addPeriodMonitor(SLOWREQEUSTLESS50);
        /**
         * 请求时间小于100毫秒
         */
        SLOWREQEUSTLESS100 = QuMonitor.createAtomicLongCounter(prefix + ".SLOWREQEUSTLESS100");
        QuMonitor.addPeriodMonitor(SLOWREQEUSTLESS100);
        /**
         * 请求时间小于1秒
         */
        SLOWREQEUSTLESS1000 = QuMonitor.createAtomicLongCounter(prefix + ".SLOWREQEUSTLESS1000");
        QuMonitor.addPeriodMonitor(SLOWREQEUSTLESS1000);
        /**
         * 请求时间大于1秒
         */
        SLOWREQEUSTMORE1000 = QuMonitor.createAtomicLongCounter(prefix + ".SLOWREQEUSTMORE1000");
        QuMonitor.addPeriodMonitor(SLOWREQEUSTMORE1000);
    }

    /**
     * 记录执行时间
     * <p>
     * 自动根据执行时间进行分区记录
     * </p>
     * 
     * @param spendtime 请求的执行时间
     */
    public void markSlowrRquests(long spendtime) {
        if (spendtime < 5) {
            SLOWREQEUSTLESS5.increment();
            return;
        }
        if (spendtime < 10) {
            SLOWREQEUSTLESS10.increment();
            return;
        }
        if (spendtime < 20) {
            SLOWREQEUSTLESS20.increment();
            return;
        }
        if (spendtime < 50) {
            SLOWREQEUSTLESS50.increment();
            return;
        }
        if (spendtime < 100) {
            SLOWREQEUSTLESS100.increment();
            return;
        }
        if (spendtime < 1000) {
            SLOWREQEUSTLESS1000.increment();
            return;
        }
        SLOWREQEUSTMORE1000.increment();
        return;
    }
}
