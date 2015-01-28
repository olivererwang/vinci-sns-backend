/*
 * $Id: MonitorNotExistsException.java 3279 2011-12-08 10:30:22Z build $
 *
 * Copyright (c) 2011 Qunar.com. All Rights Reserved.
 */

package com.vinci.common.base.monitor.exception;

/**
 * @author sunli
 */
public class MonitorNotExistsException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MonitorNotExistsException() {
        super();
    }

    public MonitorNotExistsException(String message) {
        super(message);
    }
}
