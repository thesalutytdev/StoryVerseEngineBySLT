package org.thesalutyt.storyverse.utils;

public class TimeHelper {
    private long lastMS = System.currentTimeMillis();

    public int convertToMS(int d) {
        return 1000 / d;
    }
    public long getCurrentMS()  {
        return System.currentTimeMillis();
    }
    public boolean hasReached(double milliseconds ) {
        return ((System.currentTimeMillis() - lastMS) > milliseconds);
    }
    public boolean hasTimeReached(double delay) {
        return System.currentTimeMillis() - lastMS >= delay;
    }
    public long getDelay() {
        return System.currentTimeMillis() - lastMS;
    }
    public void reset() {
        lastMS = System.currentTimeMillis();
    }
    public void reset(long def) {
        this.lastMS = System.currentTimeMillis() - def;
    }
    public void setLastMS() {
        lastMS = System.currentTimeMillis();
    }
    public void setLastMS(long lastMS) {
        this.lastMS = lastMS;
    }
    public boolean isDelayComplete(double d) {
        return this.hasReached(d);
    }
    public boolean hasPassed(double milliseconds) {
        return ((System.currentTimeMillis() - this.lastMS) > milliseconds);
    }
    public long getTime() {
        return getCurrentMS() - this.lastMS;
    }
}