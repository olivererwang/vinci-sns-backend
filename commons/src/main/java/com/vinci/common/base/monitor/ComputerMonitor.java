/*
 * $Id: ComputerMonitor.java 3279 2011-12-08 10:30:22Z build $
 * Copyright (c) 2011 Qunar.com. All Rights Reserved.
 */

package com.vinci.common.base.monitor;

/**
 * @author sunli
 */
public abstract class ComputerMonitor extends MonitorData {
    private String instanceName;
    /**
     * @return
     */
    @Override
    public final String getInstanceName() {
        return instanceName;
    }

    /**
     * @param instanceName
     */
    public final void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

}
