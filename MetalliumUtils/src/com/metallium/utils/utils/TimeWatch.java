/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.utils;

import java.util.concurrent.TimeUnit;

/**
 * 20110516
 * @author Ruben
 */
public class TimeWatch {

    long starts;

    public static TimeWatch start() {
        return new TimeWatch();
    }

    private TimeWatch() {
        reset();
    }

    public TimeWatch reset() {
        starts = System.currentTimeMillis();
        return this;
    }

    public long time() {
        return System.currentTimeMillis() - starts;
    }

    public long time(TimeUnit unit) {
        return unit.convert(time(), TimeUnit.MILLISECONDS);
    }

    /**
     * Returns the time but as a String with a more detailed description of the
     * mili seconds, seconds and minutes the task took.
     *
     * @return String
     */
    public String timeDes() {
        long millis = time();
        return String.format("%d min, %d sec, %d mili", TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(
                millis)), millis);
    }
}
