/*
 * $Id: TimeOutOfBoundsException.java 3279 2011-12-08 10:30:22Z build $
 * Copyright (c) 2011 Qunar.com. All Rights Reserved.
 */

package com.vinci.common.base.monitor.exception;

/**
 * @author sunli
 */
public class TimeOutOfBoundsException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 2045627623173374956L;

    public TimeOutOfBoundsException() {
        super();
    }

    public TimeOutOfBoundsException(String message) {
        super(message);
    }
}
