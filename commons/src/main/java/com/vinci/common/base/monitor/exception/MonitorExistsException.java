/*
 * $Id: MonitorExistsException.java 3279 2011-12-08 10:30:22Z build $
 *
 * Copyright (c) 2011 Qunar.com. All Rights Reserved.
 */

package com.vinci.common.base.monitor.exception;

/**
 * @author sunli
 */
public class MonitorExistsException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MonitorExistsException() {
        super();
    }

    public MonitorExistsException(String message) {
        super(message);
    }
}
