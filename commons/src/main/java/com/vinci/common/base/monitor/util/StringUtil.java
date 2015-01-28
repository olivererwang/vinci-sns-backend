/*
 * $Id: StringUtil.java 3279 2011-12-08 10:30:22Z build $
 * Copyright (c) 2011 Qunar.com. All Rights Reserved.
 */

package com.vinci.common.base.monitor.util;

import java.util.Map;
import java.util.Map.Entry;

import com.vinci.common.base.monitor.Monitor;
import com.vinci.common.base.monitor.QunarMonitor;

/**
 * @author sunli
 */
public class StringUtil {
    /**
       * 把监控结果map带描述信息一起输出来,用于监控系统监控
     * @param map
     * @return
     */
    public static String monitorMapToString(Map<String, Number> map) {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Number> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("\r\n");
            }
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());

        }
        return sb.toString();
    }
    /**
     * 把监控结果map带描述信息一起输出来，用于人工查看
     * @param map
     * @return
     */
    public static String monitorMapToStringWithDescription(Map<String, Number> map) {
        StringBuilder sb = new StringBuilder();
        Monitor monitor = null;
        for (Entry<String, Number> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("\r\n");
            }
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("\t//");
            monitor = QunarMonitor.getMonitor(entry.getKey());
            if (monitor != null) {
                sb.append((QunarMonitor.getMonitor(entry.getKey()).getDescription()));
            }

        }
        return sb.toString();
    }
}
