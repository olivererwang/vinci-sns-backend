/*
 * $Id: AtomicLongCounter.java 9565 2012-12-05 08:03:43Z build $ Copyright (c) 2011 Qunar.com. All Rights Reserved.
 */

package com.vinci.common.base.monitor;

import java.util.concurrent.atomic.AtomicLong;


/**
 * @author sunli
 */
public class AtomicLongCounter extends MonitorCounter {
    private final AtomicLong counter = new AtomicLong();

    @Override
    public Number getValue() {
        return counter.get();
    }

    @Override
    public void increment() {
        counter.incrementAndGet();

    }

    @Override
    public void increment(int delta) {
        counter.addAndGet(delta);

    }

    @Override
    public void decrement() {
        counter.decrementAndGet();

    }

    @Override
    public void decrement(int delta) {
        counter.addAndGet(-delta);
    }

    @Override
    public void set(Number value) {
        counter.set(value.longValue());
    }

}
