/*
 * $Id: QunarMutiIntegerMonitor.java 3279 2011-12-08 10:30:22Z build $
 * Copyright (c) 2011 Qunar.com. All Rights Reserved.
 */

package com.vinci.common.base.monitor.muti;

import com.vinci.common.base.monitor.MonitorCounter;
import com.vinci.common.base.monitor.QunarMonitor;
import com.vinci.common.base.monitor.exception.MonitorExistsException;

/**
 * Integer型的多计数器。
 * <p>
 * 比如同种类型的计数器，下属有多个计数器，需要动态创建的时候
 * </p>
 * 
 * @author sunli
 */
public class QunarMutiIntegerMonitor extends AbstractQunarMutiMonitor {

    private QunarMutiIntegerMonitor(String prefix, String description, boolean addPeriod) {
        super(prefix, description, addPeriod);
    }

    /**
     * 创建一个QunarMutiLongMonitor，counter不计算每分钟的递增区间值
     * 
     * @param prefix 前缀名称
     * @param description 描述信息
     * @return
     */
    public static QunarMutiIntegerMonitor buildWithPrefix(String prefix, String description) {
        return buildWithPrefix(prefix, description, false);
    }

    /**
     * 创建一个QunarMutiLongMonitor，counter自动计算每分钟的递增区间值
     * 
     * @param prefix 缀名称
     * @param description 描述信息
     * @param addPeriod counter是否计算每分钟的递增区间值
     * @return
     */
    public static QunarMutiIntegerMonitor buildWithPrefix(String prefix, String description, boolean addPeriod) {
        if (!instanceMap.containsKey(prefix)) {
            QunarMutiIntegerMonitor qunarMutiLongMonitor = new QunarMutiIntegerMonitor(prefix, description, addPeriod);
            if (instanceMap.putIfAbsent(prefix, qunarMutiLongMonitor) == null) {
                return qunarMutiLongMonitor;
            }

        }
        throw new MonitorExistsException("QunarMutiLongMonitor with prefix " + prefix + "  exists");
    }

    @Override
    protected MonitorCounter createMonitorCounter(String instanceName, String description) {
        return QunarMonitor.createAtomicIntegerCounter(instanceName, description);
    }
}
